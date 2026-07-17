package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.statements.ReturningStatement;

/**
 * Abstract base class for final <b>{@code CASE}</b> statements. Extends FinalStatement to support execution and <b>{@code RETURNING}</b> clauses.
 */
public abstract class CaseFinalStatement extends FinalStatement implements ReturningStatement.ReturningStatementContainer
{
    /**
     * Constructs a new CaseFinalStatement with the given query.
     *
     * @param query the <b>{@code CASE}</b> expression fragment
     */
    protected CaseFinalStatement(String query)
    {
        super(query);
    }
}
