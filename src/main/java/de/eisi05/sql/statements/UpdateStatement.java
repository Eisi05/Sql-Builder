package de.eisi05.sql.statements;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;

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
    }
}
