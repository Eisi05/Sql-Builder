package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL {@code EXISTS} condition expression.
 */
public class WhereExistsStatement extends FinalStatement
{
    /**
     * Constructs a {@code WhereExistsStatement} with the provided subquery fragment.
     *
     * @param query the SQL subquery wrapped inside the exists clause
     */
    protected WhereExistsStatement(String query)
    {
        super(query);
    }

    /**
     * Returns an empty string key, as the operation keyword is structurally integrated or built differently.
     *
     * @return an empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * A container interface providing fluent builder methods for {@code EXISTS} clauses.
     */
    public interface WhereExistsStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a {@code WHERE EXISTS} condition wrapping a detailed subquery statement.
         *
         * @param finalStatement the subquery to evaluate for existence
         * @return a configured {@link WhereExistsStatement}
         */
        default WhereExistsStatement whereExists(FinalStatement finalStatement)
        {
            return create(new WhereExistsStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}