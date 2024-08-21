package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public class CaseStatement extends AbstractStatement implements CaseWhenStatement.CaseWhenStatementContainer
{
    protected CaseStatement(String query)
    {
        super(query);
    }

    public static CaseStatement createCase()
    {
        return new CaseStatement("CASE");
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface CaseStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseStatement checkCase()
        {
            return create(new CaseStatement("CASE"));
        }
    }
}

