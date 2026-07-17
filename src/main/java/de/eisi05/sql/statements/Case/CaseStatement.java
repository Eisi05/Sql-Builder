package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL CASE expression. Used for conditional logic in SELECT statements, returning different values based on conditions. Can be used with or
 * without a case expression (simple CASE vs searched CASE).
 */
public class CaseStatement extends AbstractStatement implements CaseWhenStatementContainer
{
    /**
     * Constructs a new CaseStatement with the given query.
     *
     * @param query the CASE expression fragment
     */
    protected CaseStatement(String query)
    {
        super(query);
    }

    /**
     * Creates a simple CASE expression without a case value.
     *
     * @return a new CaseStatement
     */
    public static CaseStatement createCase()
    {
        return new CaseStatement("CASE");
    }

    /**
     * Creates a simple CASE expression with a case value.
     *
     * @param name the case value to compare against
     * @return a new CaseStatement
     */
    public static CaseStatement createCase(String name)
    {
        return new CaseStatement("CASE " + name);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since CASE doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create CASE statements.
     */
    public interface CaseStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a CASE expression without a case value.
         *
         * @return a new CaseStatement
         */
        default CaseStatement checkCase()
        {
            return create(new CaseStatement("CASE"));
        }
    }
}
