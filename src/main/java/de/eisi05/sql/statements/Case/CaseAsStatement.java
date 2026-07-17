package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a CASE expression with an AS alias. Used to alias the result of a CASE expression in SELECT statements.
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
     * Interface for containers that can create CASE AS statements.
     */
    public interface CaseAsStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a CASE AS statement with the specified alias.
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
