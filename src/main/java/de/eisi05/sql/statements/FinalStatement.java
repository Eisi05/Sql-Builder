package de.eisi05.sql.statements;

import de.eisi05.sql.exceptions.ExecutionException;
import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.result.ExecutionResult;
import de.eisi05.sql.result.QueryResult;
import de.eisi05.sql.statements.select.SelectStatement;
import org.postgresql.util.PGobject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class FinalStatement extends AbstractStatement
{
    private final List<Object> appendedParameters = new ArrayList<>();
    private String appendQuery;

    protected FinalStatement(String query)
    {
        super(query);
    }

    public FinalStatement appendStatement(FinalStatement statement)
    {
        if(appendQuery == null)
            appendQuery = getQuery();

        appendQuery = appendQuery + "\n" + statement.getQuery();
        this.appendedParameters.addAll(statement.getChainParameters());
        return this;
    }

    public ExecutionResult<Integer> executeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute an update without an execution statement like insert, update or delete");

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
        }).orElse(ExecutionResult.ofException(preparedStatement.exception()));
    }

    public ExecutionResult<Long> executeLargeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute a large update without an execution statement like insert, update or delete");

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
        }).orElse(ExecutionResult.ofException(preparedStatement.exception()));
    }

    public ExecutionResult<List<QueryResult>> executeQuery()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteQueryStatement))
            throw new ExecutionException("Cannot execute a query without a query statement like select");

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
        }).orElse(ExecutionResult.ofException(preparedStatement.exception()));
    }

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
        }).orElse(ExecutionResult.ofException(preparedStatement.exception()));
    }

    public ExecutionResult<Boolean> execute()
    {
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
        }).orElse(ExecutionResult.ofException(preparedStatement.exception()));
    }

    private ExecutionResult<PreparedStatement> createPreparedStatement()
    {
        try
        {
            PreparedStatement preparedStatement = getDatabaseStatement().getConnection().prepareStatement(getQuery());
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

    public void createStatement(BiConsumer<PreparedStatement, String> statementConsumer)
    {
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

    private void closeStatement(Statement statement)
    {
        if(statement == null)
            return;

        try
        {
            Connection connection = statement.getConnection();

            statement.close();

            if(connection != null && !connection.isClosed() && connection.getAutoCommit())
                connection.close();
        }
        catch(SQLException e)
        {
        }
    }

    @Override
    public String getQuery()
    {
        return appendQuery == null ? (super.getQuery() + ";") : appendQuery;
    }
}
