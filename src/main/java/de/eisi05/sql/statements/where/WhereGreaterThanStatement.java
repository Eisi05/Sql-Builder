package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

public class WhereGreaterThanStatement extends AbstractWhereStatement
{
    protected WhereGreaterThanStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return ">";
    }

    public interface WhereGreaterThanStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereGreaterThanStatement greaterThan(Object o)
        {
            return create(new WhereGreaterThanStatement(OrmUtils.formatValue(o)));
        }

        default WhereGreaterThanStatement greaterThan(FinalStatement finalStatement)
        {
            return create(new WhereGreaterThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
