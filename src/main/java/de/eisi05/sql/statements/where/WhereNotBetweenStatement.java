package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereNotBetweenStatement extends WhereBetweenStatement implements WhereNotStatement
{
    WhereNotBetweenStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    public interface WhereNotBetweenStatementContainer extends AbstractStatement.StatementContainer
    {
        default <T> WhereNotBetweenStatement notBetween(T t1, T t2)
        {
            if(t1 instanceof String && t2 instanceof String)
                return create(new WhereNotBetweenStatement("'" + t1 + "' AND '" + t2 + "'"));

            return create(new WhereNotBetweenStatement(t1.toString() + " AND " + t2.toString()));
        }

        default WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereNotBetweenStatement(
                    "(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            if(t2 instanceof String)
                return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND '" + t2 + "'"));

            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND " + t2.toString()));
        }
    }
}
