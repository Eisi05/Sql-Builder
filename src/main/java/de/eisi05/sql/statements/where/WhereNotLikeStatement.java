package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

public class WhereNotLikeStatement extends WhereLikeStatement implements WhereNotStatement
{
    protected WhereNotLikeStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    public interface WhereNotLikeStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotLikeStatement notLike(Object o)
        {
            return create(new WhereNotLikeStatement(OrmUtils.formatValue(o)));
        }

        default WhereNotLikeStatement notLike(FinalStatement finalStatement)
        {
            return create(new WhereNotLikeStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
