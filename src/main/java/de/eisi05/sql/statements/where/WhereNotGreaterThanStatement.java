package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

public class WhereNotGreaterThanStatement extends WhereGreaterThanStatement implements WhereNotStatement
{
    protected WhereNotGreaterThanStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return true;
    }

    public interface WhereNotGreaterStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotGreaterThanStatement notGreaterThan(Object o)
        {
            return create(new WhereNotGreaterThanStatement(OrmUtils.formatValue(o)));
        }

        default WhereNotGreaterThanStatement notGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereNotGreaterThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
