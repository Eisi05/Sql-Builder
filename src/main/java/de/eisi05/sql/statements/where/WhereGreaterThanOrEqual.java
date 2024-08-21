package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereGreaterThanOrEqual extends AbstractWhereStatement
{
    protected WhereGreaterThanOrEqual(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return ">=";
    }

    public interface WhereGreaterThanOrEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereGreaterThanOrEqual greaterThanOrEqual(Object o)
        {
            return create(new WhereGreaterThanOrEqual(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereGreaterThanOrEqual greaterThanOrEqual(FinalStatement finalStatement)
        {
            return create(new WhereGreaterThanOrEqual("(" + finalStatement.getQuery() + ")"));
        }
    }
}
