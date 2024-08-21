package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereEqualStatement extends AbstractWhereStatement
{
    protected WhereEqualStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "=";
    }

    public interface WhereEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereEqualStatement equal(Object o)
        {
            return create(new WhereEqualStatement(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereEqualStatement equal(FinalStatement finalStatement)
        {
            return create(new WhereEqualStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
