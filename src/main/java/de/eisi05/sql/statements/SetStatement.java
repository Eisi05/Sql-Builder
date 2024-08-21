package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereStatement;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SetStatement extends AbstractStatement implements WhereStatement.WhereStatementContainer
{
    private SetStatement(String column, Object value)
    {
        super(column + (value instanceof String ? " = '" + value + "'" : " = " + value));
    }

    private SetStatement(SetObject... setObjects)
    {
        super(Arrays.stream(setObjects).map(setObject -> setObject.column +
                        (setObject.value instanceof String ? " = '" + setObject.value + "'" : " = " + setObject.value))
                .collect(Collectors.joining(", ")));
    }

    private SetStatement(Map<String, Object> setObjects)
    {
        super(setObjects.entrySet().stream().map(entry -> entry.getKey() +
                (entry.getValue() instanceof String ? " = '" + entry.getValue() + "'" :
                        " = " + entry.getValue())).collect(Collectors.joining(", ")));
    }

    @Override
    protected String getKey()
    {
        return "SET";
    }

    public interface SetStatementContainer extends StatementContainer
    {
        default SetStatement set(String column, Object value)
        {
            return create(new SetStatement(column, value));
        }

        default SetStatement set(SetObject... setObjects)
        {
            return create(new SetStatement(setObjects));
        }

        default SetStatement set(Map<String, Object> objects)
        {
            return create(new SetStatement(objects));
        }
    }

    public record SetObject(String column, Object value)
    {
    }
}
