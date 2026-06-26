package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

public class WhereLessThanStatement extends AbstractWhereStatement
{
    protected WhereLessThanStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "<";
    }

    public interface WhereLessThanStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereLessThanStatement lessThan(Object o)
        {
            return create(new WhereLessThanStatement(OrmUtils.formatValue(o)));
        }

        default WhereLessThanStatement lessThan(FinalStatement finalStatement)
        {
            return create(new WhereLessThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
