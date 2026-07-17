package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL <b>{@code GREATER THAN OR EQUAL}</b> condition ({@code >=}) fragment.
 */
public class WhereGreaterThanOrEqual extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereGreaterThanOrEqual} statement with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereGreaterThanOrEqual(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL operator for <b>{@code GREATER THAN OR EQUAL}</b> to.
     *
     * @return {@code ">="}
     */
    @Override
    protected String getKey()
    {
        return ">=";
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code GREATER THAN OR EQUAL}</b> conditions.
     */
    public interface WhereGreaterThanOrEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code GREATER THAN OR EQUAL}</b> condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereGreaterThanOrEqual} statement
         */
        default WhereGreaterThanOrEqual greaterThanOrEqual(Object o)
        {
            return create(new WhereGreaterThanOrEqual("?"), o);
        }

        /**
         * Appends a <b>{@code GREATER THAN OR EQUAL}</b> condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereGreaterThanOrEqual} statement
         */
        default WhereGreaterThanOrEqual greaterThanOrEqual(FinalStatement finalStatement)
        {
            return create(new WhereGreaterThanOrEqual("(" + finalStatement.getQuery() + ")"));
        }
    }
}