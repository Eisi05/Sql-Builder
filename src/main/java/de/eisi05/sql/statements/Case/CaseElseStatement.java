package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.utils.OrmUtils;

public class CaseElseStatement extends AbstractStatement implements CaseEndStatement.CaseEndStatementContainer
{
    protected CaseElseStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "ELSE";
    }

    public interface CaseElseStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseElseStatement elseReturn(Object value)
        {
            return create(new CaseElseStatement(OrmUtils.formatValue(value)));
        }

        default CaseElseStatement elseReturn(String key)
        {
            return create(new CaseElseStatement(key));
        }
    }
}
