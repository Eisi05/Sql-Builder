package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents the THEN clause in a CASE WHEN expression. Specifies the value to return when the corresponding WHEN condition is true. Can be followed by another
 * WHEN, ELSE, or END.
 */
public class CaseThenStatement extends AbstractStatement
        implements CaseWhenStatementContainer, CaseElseStatement.CaseElseStatementContainer,
                   CaseEndStatement.CaseEndStatementContainer
{
    /**
     * Constructs a new CaseThenStatement with the given value.
     *
     * @param query the THEN value
     */
    protected CaseThenStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "THEN"
     */
    @Override
    protected String getKey()
    {
        return "THEN";
    }

    /**
     * Interface for containers that can create THEN statements.
     */
    public interface CaseThenStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a THEN statement with a direct key value.
         *
         * @param key the value to return
         * @return a new CaseThenStatement
         */
        default CaseThenStatement thenKey(String key)
        {
            return create(new CaseThenStatement(key));
        }

        /**
         * Creates a THEN statement with a parameterized value.
         *
         * @param value the value to return
         * @return a new CaseThenStatement
         */
        default CaseThenStatement thenValue(Object value)
        {
            return create(new CaseThenStatement("?"), value);
        }
    }
}
