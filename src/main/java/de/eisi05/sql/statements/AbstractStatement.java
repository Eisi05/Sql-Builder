package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.database.Database;
import de.eisi05.sql.database.OracleDatabase;
import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.statements.database.BackupDatabaseStatement;
import de.eisi05.sql.statements.database.CreateDatabaseStatement;
import de.eisi05.sql.statements.database.DropDatabaseStatement;
import de.eisi05.sql.statements.procedure.CreateProcedureStatement;
import de.eisi05.sql.statements.procedure.ExecuteProcedureStatement;
import de.eisi05.sql.statements.select.SelectStatementContainer;
import de.eisi05.sql.statements.table.*;
import de.eisi05.sql.statements.view.CreateOrReplaceViewStatement;
import de.eisi05.sql.statements.view.CreateViewStatement;
import de.eisi05.sql.statements.view.DropViewStatement;
import de.eisi05.sql.statements.where.WhereNotStatement;
import de.eisi05.sql.statements.where.WhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for all SQL statement components. Serves as the backbone for constructing complex, chained SQL queries via a fluent API. Manages
 * localized parameters, references to parent statement blocks, and coordinates database-specific dialect validation.
 */
public abstract class AbstractStatement
{
    /**
     * Local parameters bound directly to this specific statement fragment.
     */
    protected final List<Object> localParameters = new ArrayList<>();
    /**
     * Grouped parameters intended for batch-processed query execution blocks.
     */
    protected final List<List<Object>> batchParameters = new ArrayList<>();
    /**
     * The localized raw SQL query string fragment associated with this statement segment.
     */
    private final String query;
    /**
     * The preceding relational statement block linked within the current fluent invocation chain.
     */
    protected AbstractStatement parent = null;

    /**
     * Constructs a new AbstractStatement segment holding a fractional query definition.
     *
     * @param query the SQL query fragment for this statement
     */
    protected AbstractStatement(String query)
    {
        this.query = query;
    }

    /**
     * Flattens and retrieves all direct arguments compiled across every chained segment from the root node down to this node.
     *
     * @return a consolidated list containing all parameter values bound inside the sequence
     */
    public List<Object> getChainParameters()
    {
        List<Object> allParams = new ArrayList<>();
        for(AbstractStatement stmt : getAllStatements())
            allParams.addAll(stmt.localParameters);
        return allParams;
    }

    /**
     * Combines and extracts all batch arguments declared throughout the sequential hierarchy.
     *
     * @return a multi-dimensional collection containing all sequential batch parameters
     */
    public List<List<Object>> getAllBatchParameters()
    {
        List<List<Object>> allParams = new ArrayList<>();
        for(AbstractStatement stmt : getAllStatements())
            allParams.addAll(stmt.batchParameters);
        return allParams;
    }

    /**
     * Gets the operational SQL keyword designating the syntactic operation of this segment.
     *
     * @return the SQL keyword string (e.g., "SELECT", "FROM", "WHERE")
     */
    protected abstract String getKey();

    /**
     * Sequentially unrolls the backward parent chain to compile a unified, logically ordered SQL query statement string.
     *
     * @return the fully compiled structural SQL query string
     */
    protected String getQuery()
    {
        StringBuilder builder = new StringBuilder();
        AbstractStatement current = this;

        do
        {
            if(current instanceof DatabaseStatement)
                continue;

            if(current instanceof WhereNotStatement notStatement && notStatement.isNotAfterWhere() &&
                    current.parent instanceof WhereStatement statement)
                statement.withNot = true;

            if(current.query == null)
                continue;

            builder.insert(0,
                    (current instanceof WhereNotStatement notStatement && !notStatement.isNotAfterWhere() ? "NOT " :
                            "") + (current.getKey().isEmpty() ? "" : current.getKey() + " ") + current.query + " ");
        }
        while((current = current.parent) != null);

        if(builder.isEmpty())
            return "";

        return builder.substring(0, builder.length() - 1);
    }

    /**
     * Reconstructs the complete relational query path from the origin root down to this trailing node.
     *
     * @return a list containing all nodes in the chain, ordered chronologically from root to current
     */
    protected List<AbstractStatement> getAllStatements()
    {
        List<AbstractStatement> statements = new ArrayList<>();
        AbstractStatement current = this;

        do
            statements.add(current);
        while((current = current.parent) != null);

        Collections.reverse(statements);
        return statements;
    }

    /**
     * Interrogates the historical statement tree to find the original root DatabaseStatement node. This root context coordinates active connection tracking,
     * metadata retrieval, and resource release operations.
     *
     * @return the foundational DatabaseStatement anchoring this execution chain
     */
    protected DatabaseStatement getDatabaseStatement()
    {
        if(this instanceof DatabaseStatement databaseStatement)
            return databaseStatement;

        AbstractStatement current = this;
        while(!((current = current.parent) instanceof DatabaseStatement))
            ;

        return (DatabaseStatement) current;
    }

    /**
     * Appends an unmanaged, free-form custom SQL segment onto the trailing end of the statement line.
     *
     * @param query the custom SQL text to join
     * @return a new CustomStatement tracking instance bound to this segment
     */
    public CustomStatement addCustom(String query)
    {
        CustomStatement statement = new CustomStatement(query);
        statement.setParent(this);
        return statement;
    }

    /**
     * Assigns the preceding node context to maintain the backwards-linked structural tree.
     *
     * @param parent the target parent node segment to link
     * @param <T>    the structural type constraint of the parent statement
     */
    protected <T extends AbstractStatement> void setParent(T parent)
    {
        this.parent = parent;
    }

    /**
     * Interface for containers that can create and manage SQL statements. Provides default methods for creating statements with parameters and database
     * validation.
     */
    public interface StatementContainer
    {
        /**
         * Creates a statement with the given parameters.
         *
         * @param statement  the statement to create
         * @param parameters the parameters to add to the statement
         * @param <T>        the type of the statement
         * @return the created statement with parameters
         */
        default <T extends AbstractStatement> T create(T statement, Object... parameters)
        {
            if(parameters != null && parameters.length > 0)
                statement.localParameters.addAll(Arrays.stream(parameters).map(OrmUtils::cleanParameter).toList());
            return create(statement);
        }

        /**
         * Creates a statement with the given collection of parameters.
         *
         * @param statement  the statement to create
         * @param parameters the collection of parameters to add to the statement
         * @param <T>        the type of the statement
         * @return the created statement with parameters
         */
        default <T extends AbstractStatement> T create(T statement, Collection<?> parameters)
        {
            if(parameters != null && !parameters.isEmpty())
                statement.localParameters.addAll(parameters.stream().map(OrmUtils::cleanParameter).toList());
            return create(statement);
        }

        /**
         * Creates a statement and validates it against the database type.
         *
         * @param t   the statement to create
         * @param <T> the type of the statement
         * @return the created and validated statement
         * @throws UnsupportedOperationException if the database type doesn't support this statement
         */
        default <T extends AbstractStatement> T create(T t)
        {
            if(this instanceof AbstractStatement statement)
            {
                t.setParent(statement);

                SqlData sqlData = t.getClass().getAnnotation(SqlData.class);
                if(sqlData != null)
                {
                    boolean match = Arrays.stream(sqlData.value())
                            .anyMatch(databaseType -> t.getDatabaseStatement().getDatabase()
                                    .map(database ->
                                    {
                                        if(database.getDatabaseType() == databaseType &&
                                                databaseType == DatabaseType.ORACLE)
                                            return sqlData.oracleVersion() >= ((OracleDatabase) database).getVersion();
                                        return database.getDatabaseType() == databaseType;
                                    }).orElse(false));

                    if(!match)
                        throw new UnsupportedOperationException(
                                "The database type does not support this statement (" + t.getClass().getSimpleName() +
                                        ")! Required: " + Arrays.stream(sqlData.value()).map(databaseType ->
                                {
                                    if(databaseType == DatabaseType.ORACLE)
                                        return databaseType.name() + " (>=" + sqlData.oracleVersion() + ")";
                                    return databaseType.name();
                                }).collect(Collectors.joining(" or ")));
                }
            }
            return t;
        }

        /**
         * Gets the database associated with this container.
         *
         * @return an Optional containing the database, or empty if not available
         */
        default Optional<Database> getDatabase()
        {
            if(this instanceof AbstractStatement statement)
                return Optional.ofNullable(statement.getDatabaseStatement().database);
            return Optional.empty();
        }
    }

    /**
     * Interface that aggregates all default statement container interfaces. Provides a single point for accessing all statement creation methods.
     */
    public interface DefaultStatementContainers extends SelectStatementContainer,
                                                        InsertIntoStatement.InsertIntoStatementContainer,
                                                        UpdateStatement.UpdateStatementContainer,
                                                        DeleteStatement.DeleteStatementContainer,
                                                        CreateProcedureStatement.CreateProcedureStatementContainer,
                                                        ExecuteProcedureStatement.ExecuteProcedureStatementContainer,
                                                        CreateDatabaseStatement.CreateDataBaseStatementContainer,
                                                        DropDatabaseStatement.DropDatabaseStatementContainer,
                                                        BackupDatabaseStatement.BackupDatabaseStatementContainer,
                                                        CreateTableStatement.CreateTableStatementContainer,
                                                        DropTableStatement.DropTableStatementContainer,
                                                        TruncateTableStatement.TruncateTableStatementContainer,
                                                        CreateIndexStatement.CreateIndexStatementContainer,
                                                        DropIndexStatement.DropIndexStatementContainer,
                                                        CreateViewStatement.CreateViewStatementContainer,
                                                        CreateOrReplaceViewStatement.CreateOrReplaceViewStatementContainer,
                                                        DropViewStatement.DropViewStatementContainer,
                                                        AlterTableStatement.AlterTableStatementContainer,
                                                        MigrateStatement.MigrateStatementContainer
    {
    }
}