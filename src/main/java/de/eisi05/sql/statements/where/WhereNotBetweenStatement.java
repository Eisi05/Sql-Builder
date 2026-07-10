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
            return create(new WhereNotBetweenStatement("? AND ?"), t1, t2);
        }

        default WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND ?"), t2);
        }
    }
}
