package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereLessThanOrEqualStatement extends AbstractWhereStatement
{
    protected WhereLessThanOrEqualStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "<=";
    }

    public interface WhereLessOrEqualThanStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereLessThanOrEqualStatement lessThanOrEqual(Object o)
        {
            return create(new WhereLessThanOrEqualStatement(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereLessThanOrEqualStatement lessThanOrEqual(FinalStatement finalStatement)
        {
            return create(new WhereLessThanOrEqualStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
