package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereNotLessThanStatement extends WhereLessThanStatement implements WhereNotStatement
{
    protected WhereNotLessThanStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return true;
    }

    public interface WhereNotLessStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotLessThanStatement notLess(Object o)
        {
            return create(new WhereNotLessThanStatement(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereNotLessThanStatement notLess(FinalStatement finalStatement)
        {
            return create(new WhereNotLessThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
