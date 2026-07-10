package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereStatement;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SetStatement extends AbstractStatement implements WhereStatement.WhereStatementContainer
{
    private SetStatement(String column)
    {
        super(column + " = ?");
    }

    private SetStatement(SetObject... setObjects)
    {
        super(Arrays.stream(setObjects).map(setObject -> setObject.column + " = ?").collect(Collectors.joining(", ")));
    }

    private SetStatement(Map<String, Object> setObjects)
    {
        super(setObjects.keySet().stream().map(key -> key + " = ?").collect(Collectors.joining(", ")));
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
            return create(new SetStatement(column), value);
        }

        default SetStatement set(SetObject... setObjects)
        {
            return create(new SetStatement(setObjects), Arrays.stream(setObjects).map(setObject -> setObject.value).toList());
        }

        default SetStatement set(Map<String, Object> objects)
        {
            return create(new SetStatement(objects), objects.values());
        }
    }

    public record SetObject(String column, Object value)
    {
    }
}
