package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents the <b>{@code END}</b> clause of a <b>{@code CASE}</b> expression. Terminates the <b>{@code CASE}</b> expression and optionally allows aliasing
 * with AS.
 */
public class CaseEndStatement extends CaseFinalStatement implements CaseAsStatement.CaseAsStatementContainer
{
    /**
     * Constructs a new CaseEndStatement.
     *
     * @param query the <b>{@code END}</b> keyword
     */
    protected CaseEndStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since <b>{@code END}</b> doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create <b>{@code END}</b> statements.
     */
    public interface CaseEndStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates an <b>{@code END}</b> statement to terminate the <b>{@code CASE}</b> expression.
         *
         * @return a new CaseEndStatement
         */
        default CaseEndStatement end()
        {
            return create(new CaseEndStatement("END"));
        }
    }
}
