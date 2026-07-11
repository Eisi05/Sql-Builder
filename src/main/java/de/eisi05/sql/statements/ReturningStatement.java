package de.eisi05.sql.statements;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;

import java.util.Collection;

public class ReturningStatement extends FinalStatement implements ExecuteQueryStatement
{
    protected ReturningStatement(String key)
    {
        super(key);
    }

    @Override
    protected String getKey()
    {
        return "RETURNING";
    }

    public interface ReturningStatementContainer extends StatementContainer
    {
        default ReturningStatement returning(String key)
        {
            return create(new ReturningStatement(key));
        }

        default ReturningStatement returning(Collection<String> keys)
        {
            return create(new ReturningStatement(String.join(", ", keys)));
        }

        default ReturningStatement returning(String[] keys)
        {
            return create(new ReturningStatement(String.join(", ", keys)));
        }
    }
}
