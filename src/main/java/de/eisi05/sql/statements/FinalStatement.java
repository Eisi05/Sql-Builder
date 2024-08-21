package de.eisi05.sql.statements;

import de.eisi05.sql.exceptions.ExecutionException;
import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.result.ExecutionResult;
import de.eisi05.sql.result.QueryResult;
import de.eisi05.sql.statements.select.SelectStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class FinalStatement extends AbstractStatement
{
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
        return this;
    }

    public ExecutionResult<Integer> executeUpdate()
    {
        if(getAllStatements().stream().noneMatch(
                statement -> statement instanceof ExecuteUpdateStatement))
            throw new ExecutionException(
                    "Can't execute an update without an execution statement like insert, update or delete");

        return createStatement().map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeUpdate(getQuery()));
            } catch(SQLException e)
            {
                return ExecutionResult.<Integer>ofException(new RuntimeException(e));
            } finally
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

        return createStatement().map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.executeLargeUpdate(getQuery()));
            } catch(SQLException e)
            {
                return ExecutionResult.<Long>ofException(new RuntimeException(e));
            } finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.empty());
    }

    public ExecutionResult<QueryResult> executeQuery()
    {
        if(getAllStatements().stream().noneMatch(statement -> statement instanceof ExecuteQueryStatement))
            throw new ExecutionException("Cannot execute a query without a query statement like select");

        return createStatement().map(statement ->
        {
            try
            {
                ResultSet rs = statement.executeQuery(getQuery());
                return ExecutionResult.of(new QueryResult(rs));
            } catch(SQLException e)
            {
                return ExecutionResult.<QueryResult>ofException(new RuntimeException(e));
            } finally
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

        return createStatement().map(statement ->
        {
            try(ResultSet rs = statement.executeQuery(getQuery()))
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
            } catch(SQLException e)
            {
                return ExecutionResult.<T>ofException(new RuntimeException(e));
            } finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.empty());
    }

    public ExecutionResult<Boolean> execute()
    {
        return createStatement().map(statement ->
        {
            try
            {
                return ExecutionResult.of(statement.execute(getQuery()));
            } catch(SQLException e)
            {
                return ExecutionResult.<Boolean>ofException(new RuntimeException());
            } finally
            {
                closeStatement(statement);
            }
        }).orElse(ExecutionResult.empty());
    }

    private Optional<Statement> createStatement()
    {
        try
        {
            return Optional.ofNullable(getDatabaseStatement().connection.createStatement());
        } catch(SQLException e)
        {
            return Optional.empty();
        }
    }

    public void createStatement(BiConsumer<Statement, String> statementConsumer)
    {
        statementConsumer.accept(
                createStatement().orElseThrow(() -> new RuntimeException("Statement cannot be created")), getQuery());
    }

    private void closeStatement(Statement statement)
    {
        try
        {
            statement.close();
        } catch(SQLException e)
        {
        }
    }

    @Override
    public String getQuery()
    {
        return appendQuery == null ? (super.getQuery() + ";") : appendQuery;
    }
}
