package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

@SqlData(DatabaseType.MYSQL)
public class LimitStatement extends FinalStatement
{
    private LimitStatement(long amount)
    {
        super(String.valueOf(amount));
    }

    @Override
    protected String getKey()
    {
        return "LIMIT";
    }

    public interface LimitStatementContainer extends StatementContainer
    {
        default LimitStatement limit(long amount)
        {
            return create(new LimitStatement(amount));
        }
    }
}
