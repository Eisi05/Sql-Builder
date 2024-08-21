package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

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
            if(value instanceof String)
                return create(new CaseElseStatement("'" + value + "'"));
            else
                return create(new CaseElseStatement(value.toString()));
        }

        default CaseElseStatement elseReturn(String key)
        {
            return create(new CaseElseStatement(key));
        }
    }
}
