package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereBetweenStatement extends AbstractWhereStatement
{
    WhereBetweenStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "BETWEEN";
    }

    public interface WhereBetweenStatementContainer extends AbstractStatement.StatementContainer
    {
        default <T> WhereBetweenStatement between(T t1, T t2)
        {
            if(t1 instanceof String && t2 instanceof String)
                return create(new WhereBetweenStatement("'" + t1 + "' AND '" + t2 + "'"));

            return create(new WhereBetweenStatement(t1.toString() + " AND " + t2.toString()));
        }

        default WhereBetweenStatement between(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereBetweenStatement(
                    "(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> WhereBetweenStatement between(FinalStatement finalStatement1, T t2)
        {
            if(t2 instanceof String)
                return create(new WhereBetweenStatement("(" + finalStatement1.getQuery() + ") AND '" + t2 + "'"));

            return create(new WhereBetweenStatement("(" + finalStatement1.getQuery() + ") AND " + t2.toString()));
        }
    }
}

