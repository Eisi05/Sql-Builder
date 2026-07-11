package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.statements.ReturningStatement;

public abstract class CaseFinalStatement extends FinalStatement implements ReturningStatement.ReturningStatementContainer
{
    protected CaseFinalStatement(String query)
    {
        super(query);
    }
}
