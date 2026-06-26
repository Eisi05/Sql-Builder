package de.eisi05.sql.statements;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Map;

public class UpdateStatement extends AbstractStatement implements SetStatement.SetStatementContainer,
                                                                  ExecuteUpdateStatement
{
    private UpdateStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "UPDATE";
    }

    public interface UpdateStatementContainer extends StatementContainer
    {
        default UpdateStatement update(String table)
        {
            return create(new UpdateStatement(table));
        }

        default <T> SetStatement update(T object)
        {
            UpdateStatement updateStatement = new UpdateStatement(OrmUtils.resolveTable(object.getClass()));
            Map<String, Object> values = OrmUtils.toColumnMap(object);
            return create(updateStatement.set(values));
        }
    }
}
