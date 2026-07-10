package de.eisi05.sql.statements;

import de.eisi05.sql.exceptions.ExecutionException;
import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.result.ExecutionResult;
import de.eisi05.sql.result.QueryResult;
import de.eisi05.sql.statements.select.SelectStatement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

        return createPreparedStatement().map(statement ->
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
        }).orElse(ExecutionResult.empty());
    }

    public ExecutionResult<Long> executeLargeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute a large update without an execution statement like insert, update or delete");

        return createPreparedStatement().map(statement ->
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
        }).orElse(ExecutionResult.empty());
    }

    public ExecutionResult<List<QueryResult>> executeQuery()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteQueryStatement))
            throw new ExecutionException("Cannot execute a query without a query statement like select");

        return createPreparedStatement().map(statement ->
        {
            try
            {
                ResultSet rs = statement.executeQuery();

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
        }).orElse(ExecutionResult.empty());
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

        return createPreparedStatement().map(statement ->
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
        }).orElse(ExecutionResult.empty());
    }

    public ExecutionResult<Boolean> execute()
    {
        return createPreparedStatement().map(statement ->
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
        }).orElse(ExecutionResult.empty());
    }

    private Optional<PreparedStatement> createPreparedStatement()
    {
        try
        {
            PreparedStatement preparedStatement = getDatabaseStatement().getConnection().prepareStatement(getQuery());
            List<Object> totalParams = new ArrayList<>(getChainParameters());
            totalParams.addAll(appendedParameters);
            for(int i = 0; i < totalParams.size(); i++)
                preparedStatement.setObject(i + 1, totalParams.get(i));
            return Optional.of(preparedStatement);
        }
        catch(SQLException e)
        {
            return Optional.empty();
        }
    }

    public void createStatement(BiConsumer<PreparedStatement, String> statementConsumer)
    {
        statementConsumer.accept(createPreparedStatement().orElseThrow(() -> new RuntimeException("Statement cannot be created")), getQuery());
    }

    private void closeStatement(Statement statement)
    {
        try
        {
            statement.close();
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
