package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a <b>{@code CASE}</b> expression with an AS alias. Used to alias the result of a <b>{@code CASE}</b> expression in <b>{@code SELECT}</b> statements.
 */
public class CaseAsStatement extends CaseFinalStatement
{
    /**
     * Constructs a new CaseAsStatement with the given alias.
     *
     * @param query the alias name
     */
    protected CaseAsStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "AS"
     */
    @Override
    protected String getKey()
    {
        return "AS";
    }

    /**
     * Interface for containers that can create <b>{@code CASE AS}</b> statements.
     */
    public interface CaseAsStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code CASE AS}</b> statement with the specified alias.
         *
         * @param key the alias name
         * @return a new CaseAsStatement
         */
        default CaseAsStatement as(String key)
        {
            return create(new CaseAsStatement(key));
        }
    }
}
