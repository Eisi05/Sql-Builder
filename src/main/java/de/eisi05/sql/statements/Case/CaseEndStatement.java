package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents the END clause of a CASE expression. Terminates the CASE expression and optionally allows aliasing with AS.
 */
public class CaseEndStatement extends CaseFinalStatement implements CaseAsStatement.CaseAsStatementContainer
{
    /**
     * Constructs a new CaseEndStatement.
     *
     * @param query the END keyword
     */
    protected CaseEndStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since END doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create END statements.
     */
    public interface CaseEndStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates an END statement to terminate the CASE expression.
         *
         * @return a new CaseEndStatement
         */
        default CaseEndStatement end()
        {
            return create(new CaseEndStatement("END"));
        }
    }
}
