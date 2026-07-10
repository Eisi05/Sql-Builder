package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public class CaseThenStatement extends AbstractStatement
        implements CaseWhenStatementContainer, CaseElseStatement.CaseElseStatementContainer,
                   CaseEndStatement.CaseEndStatementContainer
{
    protected CaseThenStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "THEN";
    }

    public interface CaseThenStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseThenStatement thenKey(String key)
        {
            return create(new CaseThenStatement(key));
        }

        default CaseThenStatement thenValue(Object value)
        {
            return create(new CaseThenStatement("?"), value);
        }
    }
}

