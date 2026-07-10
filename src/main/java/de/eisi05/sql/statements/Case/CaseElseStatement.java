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
            CaseElseStatement statement = create(new CaseElseStatement("?"));
            statement.localParameters.add(value);
            return statement;
        }

        default CaseElseStatement elseReturn(String key)
        {
            return create(new CaseElseStatement(key));
        }
    }
}
