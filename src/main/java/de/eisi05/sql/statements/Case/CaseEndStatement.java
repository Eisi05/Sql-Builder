package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public class CaseEndStatement extends CaseFinalStatement implements CaseAsStatement.CaseAsStatementContainer
{
    protected CaseEndStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface CaseEndStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseEndStatement end()
        {
            return create(new CaseEndStatement("END"));
        }
    }
}
