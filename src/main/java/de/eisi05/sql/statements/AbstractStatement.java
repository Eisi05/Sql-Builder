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

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractStatement
{
    private final String query;

    protected AbstractStatement parent = null;

    protected AbstractStatement(String query)
    {
        this.query = query;
    }

    protected abstract String getKey();

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

            builder.insert(0,
                    (current instanceof WhereNotStatement notStatement && !notStatement.isNotAfterWhere() ? "NOT " :
                            "") + (current.getKey().isEmpty() ? "" : current.getKey() + " ") + current.query + " ");
        }
        while((current = current.parent) != null);

        return builder.substring(0, builder.length() - 1);
    }

    protected List<AbstractStatement> getAllStatements()
    {
        List<AbstractStatement> statements = new ArrayList<>();
        AbstractStatement current = this;

        do statements.add(current);
        while((current = current.parent) != null);

        Collections.reverse(statements);
        return statements;
    }

    protected DatabaseStatement getDatabaseStatement()
    {
        if(this instanceof DatabaseStatement databaseStatement)
            return databaseStatement;

        AbstractStatement current = this;
        while(!((current = current.parent) instanceof DatabaseStatement)) ;

        return (DatabaseStatement) current;
    }

    protected <T extends AbstractStatement> void setParent(T parent)
    {
        this.parent = parent;
    }

    public interface StatementContainer
    {
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

        default Optional<Database> getDatabase()
        {
            if(this instanceof AbstractStatement statement)
                return Optional.ofNullable(statement.getDatabaseStatement().database);
            return Optional.empty();
        }
    }

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
            AlterTableStatement.AlterTableStatementContainer
    {
    }
}
