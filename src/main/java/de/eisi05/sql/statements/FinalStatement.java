package de.eisi05.sql.statements;

import de.eisi05.sql.exceptions.ExecutionException;
import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.result.ExecutionResult;
import de.eisi05.sql.result.QueryResult;
import de.eisi05.sql.statements.select.SelectStatement;
import de.eisi05.sql.utils.OrmUtils;
import org.postgresql.util.PGobject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Abstract base class for executable SQL statements. Provides methods for executing queries, updates, and batch operations. Handles PreparedStatement creation
 * and parameter binding.
 */
public abstract class FinalStatement extends AbstractStatement
{
    /**
     * Parameters from appended statements.
     */
    private final List<Object> appendedParameters = new ArrayList<>();
    /**
     * The complete query after appending statements.
     */
    private String appendQuery;

    /**
     * Constructs a new FinalStatement with the given query.
     *
     * @param query the SQL query fragment
     */
    protected FinalStatement(String query)
    {
        super(query);
    }

    /**
     * Appends another statement to this one. The queries are concatenated with newlines and parameters are merged.
     *
     * @param statement the statement to append
     * @return this statement for chaining
     */
    public FinalStatement appendStatement(FinalStatement statement)
    {
        if(appendQuery == null && !getQuery().isEmpty())
            appendQuery = getQuery();

        if(appendQuery == null)
            appendQuery = statement.getQuery();
        else
            appendQuery = appendQuery + "\n" + statement.getQuery();
        this.appendedParameters.addAll(statement.getChainParameters());
        return this;
    }

    /**
     * Checks if this statement has batch parameters.
     *
     * @return true if batch parameters are present, false otherwise
     */
    public boolean isBatch()
    {
        return getAllBatchParameters() != null && !getAllBatchParameters().isEmpty();
    }

    /**
     * Checks if the statement chain contains a MigrateStatement.
     *
     * @return true if a MigrateStatement is present, false otherwise
     */
    private boolean containsMigrateStatement()
    {
        return this instanceof MigrateStatement || getAllStatements().stream().anyMatch(s -> s instanceof MigrateStatement);
    }

    /**
     * Executes a batch update operation.
     *
     * @return an ExecutionResult containing the array of update counts
     * @throws ExecutionException if the statement doesn't support updates
     */
    public ExecutionResult<int[]> executeBatchUpdate()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException("Can't execute a batch update without an update/insert statement");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(new int[0]);

        if(!isBatch())
            return executeUpdate().map(integer -> new int[]{integer});

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeBatch());
            }
            catch(SQLException e)
            {
                return ExecutionResult.<int[]>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes a large batch update operation.
     *
     * @return an ExecutionResult containing the array of update counts as long values
     * @throws ExecutionException if the statement doesn't support updates
     */
    public ExecutionResult<long[]> executeLargeBatchUpdate()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException("Can't execute a batch update without an update/insert statement");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(new long[0]);

        if(!isBatch())
            return executeLargeUpdate().map(l -> new long[]{l});

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeLargeBatch());
            }
            catch(SQLException e)
            {
                return ExecutionResult.<long[]>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes a single update operation.
     *
     * @return an ExecutionResult containing the number of affected rows
     * @throws ExecutionException if the statement doesn't support updates or has batch parameters
     */
    public ExecutionResult<Integer> executeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute an update without an execution statement like insert, update or delete");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(0);

        if(isBatch())
            throw new ExecutionException("This statement contains batch parameters. Use executeBatchUpdate() instead.");

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeUpdate());
            }
            catch(SQLException e)
            {
                return ExecutionResult.<Integer>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes a single large update operation.
     *
     * @return an ExecutionResult containing the number of affected rows as a long value
     * @throws ExecutionException if the statement doesn't support updates or has batch parameters
     */
    public ExecutionResult<Long> executeLargeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute a large update without an execution statement like insert, update or delete");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(0L);

        if(isBatch())
            throw new ExecutionException("This statement contains batch parameters. Use executeBatchUpdate() instead.");

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeLargeUpdate());
            }
            catch(SQLException e)
            {
                return ExecutionResult.<Long>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes a query operation and returns all results.
     *
     * @return an ExecutionResult containing a list of QueryResult objects
     * @throws ExecutionException if the statement doesn't support queries
     */
    public ExecutionResult<List<QueryResult>> executeQuery()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteQueryStatement))
            throw new ExecutionException("Cannot execute a query without a query statement like select");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(new ArrayList<>());

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try(ResultSet rs = statement.executeQuery())
            {
                List<QueryResult> results = new ArrayList<>();
                while(rs.next())
                    results.add(new QueryResult(rs));

                return ExecutionResult.of(results);
            }
            catch(SQLException e)
            {
                return ExecutionResult.<List<QueryResult>>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes a query operation and returns a single value of the specified type.
     *
     * @param type the SQL data type for the result
     * @param <T>  the type of the result
     * @return an ExecutionResult containing the single value
     * @throws ExecutionException if the statement doesn't support queries or has multiple keys
     */
    public <T> ExecutionResult<T> executeQuery(SqlDataType<T> type)
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof SelectStatement))
            throw new ExecutionException("Cannot execute a query without a query statement like select");

        if(getAllStatements().stream().filter(statement -> statement instanceof SelectStatement)
                .map(abstractStatement -> (SelectStatement) abstractStatement)
                .anyMatch(statement -> statement.getKeys().length > 1 ||
                        Arrays.asList(statement.getKeys()).contains("*")))
            throw new ExecutionException(
                    "Cannot execute a query with data type with multiple keys in the Select statement");

        if(!(type instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSQLDataType))
            throw new ExecutionException("Use a type from the SqlDataType class");

        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(null);

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try(ResultSet rs = statement.executeQuery())
            {
                rs.next();
                String key = getAllStatements().stream()
                        .filter(abstractStatement -> abstractStatement instanceof SelectStatement)
                        .map(abstractStatement -> (SelectStatement) abstractStatement)
                        .filter(selectStatement -> selectStatement.getKeys().length == 1)
                        .findFirst()
                        .map(abstractStatement -> abstractStatement.getKeys()[0])
                        .orElseThrow(() -> new ExecutionException("No key found"));
                return ExecutionResult.of(rs.getObject(key, primitiveSQLDataType.getDataType()));
            }
            catch(SQLException e)
            {
                return ExecutionResult.<T>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Executes the statement without returning a specific result type.
     *
     * @return an ExecutionResult containing a boolean indicating success
     */
    public ExecutionResult<Boolean> execute()
    {
        if(getQuery().isEmpty() && containsMigrateStatement())
            return ExecutionResult.of(true);

        ExecutionResult<PreparedStatement> preparedStatement = createPreparedStatement();
        return preparedStatement.map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.execute());
            }
            catch(SQLException e)
            {
                return ExecutionResult.<Boolean>ofException(new RuntimeException(e));
            }
            finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.ofException(preparedStatement.hasException() ? preparedStatement.exception() :
                new RuntimeException("Failed to create statement")));
    }

    /**
     * Creates a PreparedStatement with the current query and parameters. Handles both regular and batch parameter binding.
     *
     * @return an ExecutionResult containing the PreparedStatement
     */
    private ExecutionResult<PreparedStatement> createPreparedStatement()
    {
        try
        {
            PreparedStatement preparedStatement = getDatabaseStatement().getConnection().prepareStatement(getQuery());

            if(isBatch())
            {
                for(List<Object> rowParams : getAllBatchParameters())
                {
                    for(int i = 0; i < rowParams.size(); i++)
                    {
                        Object param = OrmUtils.cleanParameter(rowParams.get(i));
                        if(param instanceof PGobject)
                            preparedStatement.setObject(i + 1, param, Types.OTHER);
                        else
                            preparedStatement.setObject(i + 1, param);
                    }
                    preparedStatement.addBatch();
                }
                return ExecutionResult.of(preparedStatement);
            }

            List<Object> totalParams = new ArrayList<>(getChainParameters());
            totalParams.addAll(appendedParameters);
            for(int i = 0; i < totalParams.size(); i++)
            {
                Object param = totalParams.get(i);

                if(param instanceof PGobject)
                    preparedStatement.setObject(i + 1, param, Types.OTHER);
                else
                    preparedStatement.setObject(i + 1, param);
            }
            return ExecutionResult.of(preparedStatement);
        }
        catch(SQLException e)
        {
            return ExecutionResult.ofException(new RuntimeException(e));
        }
    }

    /**
     * Creates a PreparedStatement and passes it to the given consumer. The consumer receives both the statement and the query string.
     *
     * @param statementConsumer the consumer to handle the PreparedStatement
     */
    public void createStatement(BiConsumer<PreparedStatement, String> statementConsumer)
    {
        if(getQuery().isEmpty() && containsMigrateStatement())
            return;

        PreparedStatement preparedStatement = createPreparedStatement().orElseThrow();
        try
        {
            statementConsumer.accept(preparedStatement, getQuery());
        }
        finally
        {
            closeStatement(preparedStatement);
        }
    }

    /**
     * Closes the provided SQL statement resource and delegates the release of its underlying database connection back to the root database container lifecycle
     * management.
     *
     * @param statement the statement to close
     */
    private void closeStatement(Statement statement)
    {
        if(statement == null)
            return;

        try
        {
            Connection connection = statement.getConnection();

            statement.close();

            DatabaseStatement databaseStatement = getDatabaseStatement();
            if(connection != null && databaseStatement != null)
                databaseStatement.releaseConnection(connection);
        }
        catch(SQLException e)
        {
        }
    }

    /**
     * Gets the complete SQL query. Returns the appended query if statements were appended, otherwise returns the base query with a semicolon.
     *
     * @return the complete SQL query string
     */
    @Override
    public String getQuery()
    {
        if(appendQuery == null && super.getQuery().isEmpty())
            return "";
        else if(appendQuery == null)
            return super.getQuery() + ";";
        return appendQuery;
    }
}
