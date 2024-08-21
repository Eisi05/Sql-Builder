package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereDefaultStatementContainer;

public class HavingStatement extends FinalStatement implements WhereDefaultStatementContainer
{
    protected HavingStatement(String key)
    {
        super(key);
    }

    @Override
    protected String getKey()
    {
        return "HAVING";
    }

    public interface HavingStatementContainer extends StatementContainer
    {
        default HavingStatement hasCount(String key)
        {
            return create(new HavingStatement("COUNT(" + key + ")"));
        }
    }
}
