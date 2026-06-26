package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

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
            return create(new WhereNotBetweenStatement(OrmUtils.formatValue(t1) + " AND " + OrmUtils.formatValue(t2)));
        }

        default WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND " + OrmUtils.formatValue(t2)));
        }
    }
}
