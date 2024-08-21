package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereLikeStatement extends AbstractWhereStatement
{
    protected WhereLikeStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "LIKE";
    }

    public interface WhereLikeStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereLikeStatement like(Object o)
        {
            return create(new WhereLikeStatement(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereLikeStatement like(FinalStatement finalStatement)
        {
            return create(new WhereLikeStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
