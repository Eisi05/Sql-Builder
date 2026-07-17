package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents the <b>{@code THEN}</b> clause in a <b>{@code CASE WHEN}</b> expression. Specifies the value to return when the corresponding <b>{@code WHEN}</b>
 * condition is true. Can be followed by another
 * <b>{@code WHEN}</b> , <b>{@code ELSE}</b> , or <b>{@code END}</b> .
 */
public class CaseThenStatement extends AbstractStatement
        implements CaseWhenStatementContainer, CaseElseStatement.CaseElseStatementContainer,
                   CaseEndStatement.CaseEndStatementContainer
{
    /**
     * Constructs a new CaseThenStatement with the given value.
     *
     * @param query the <b>{@code THEN}</b> value
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
     * Interface for containers that can create <b>{@code THEN}</b> statements.
     */
    public interface CaseThenStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code THEN}</b> statement with a direct key value.
         *
         * @param key the value to return
         * @return a new CaseThenStatement
         */
        default CaseThenStatement thenKey(String key)
        {
            return create(new CaseThenStatement(key));
        }

        /**
         * Creates a <b>{@code THEN}</b> statement with a parameterized value.
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
