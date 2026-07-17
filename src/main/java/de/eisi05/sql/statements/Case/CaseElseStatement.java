package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents the ELSE clause in a CASE expression. Provides a default value when no WHEN conditions match.
 */
public class CaseElseStatement extends AbstractStatement implements CaseEndStatement.CaseEndStatementContainer
{
    /**
     * Constructs a new CaseElseStatement with the given value.
     *
     * @param query the ELSE value
     */
    protected CaseElseStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "ELSE"
     */
    @Override
    protected String getKey()
    {
        return "ELSE";
    }

    /**
     * Interface for containers that can create ELSE statements.
     */
    public interface CaseElseStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates an ELSE statement with a parameterized value.
         *
         * @param value the default value
         * @return a new CaseElseStatement
         */
        default CaseElseStatement elseReturn(Object value)
        {
            CaseElseStatement statement = create(new CaseElseStatement("?"));
            statement.localParameters.add(value);
            return statement;
        }

        /**
         * Creates an ELSE statement with a direct value.
         *
         * @param key the default value
         * @return a new CaseElseStatement
         */
        default CaseElseStatement elseReturn(String key)
        {
            return create(new CaseElseStatement(key));
        }
    }
}
