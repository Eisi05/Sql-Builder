package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code CASE}</b> expression. Used for conditional logic in <b>{@code SELECT}</b> statements, returning different values based on
 * conditions. Can be used with or without a case expression (simple <b>{@code CASE}</b> vs searched <b>{@code CASE}</b> ).
 */
public class CaseStatement extends AbstractStatement implements CaseWhenStatementContainer
{
    /**
     * Constructs a new CaseStatement with the given query.
     *
     * @param query the <b>{@code CASE}</b> expression fragment
     */
    protected CaseStatement(String query)
    {
        super(query);
    }

    /**
     * Creates a simple <b>{@code CASE}</b> expression without a case value.
     *
     * @return a new CaseStatement
     */
    public static CaseStatement createCase()
    {
        return new CaseStatement("CASE");
    }

    /**
     * Creates a simple <b>{@code CASE}</b> expression with a case value.
     *
     * @param name the case value to compare against
     * @return a new CaseStatement
     */
    public static CaseStatement createCase(String name)
    {
        return new CaseStatement("CASE " + name);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since <b>{@code CASE}</b> doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create <b>{@code CASE}</b> statements.
     */
    public interface CaseStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code CASE}</b> expression without a case value.
         *
         * @return a new CaseStatement
         */
        default CaseStatement checkCase()
        {
            return create(new CaseStatement("CASE"));
        }
    }
}
