package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public class CaseThenStatement extends AbstractStatement
        implements CaseWhenStatement.CaseWhenStatementContainer, CaseElseStatement.CaseElseStatementContainer,
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
}

