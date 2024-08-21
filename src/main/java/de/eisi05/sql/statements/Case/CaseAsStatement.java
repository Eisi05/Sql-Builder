package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public class CaseAsStatement extends CaseFinalStatement
{
    protected CaseAsStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "AS";
    }

    public interface CaseAsStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseAsStatement as(String key)
        {
            return create(new CaseAsStatement(key));
        }
    }
}
