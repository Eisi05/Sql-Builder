package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL <b>{@code EQUAL}</b> condition ({@code =}) fragment.
 */
public class WhereEqualStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereEqualStatement} with the given query expression.
     *
     * @param query the SQL query target expression (e.g., placeholders or subqueries)
     */
    protected WhereEqualStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL <b>{@code EQUAL}</b> operator.
     *
     * @return {@code "="}
     */
    @Override
    protected String getKey()
    {
        return "=";
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code EQUAL}</b> conditions.
     */
    public interface WhereEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an <b>{@code EQUAL}</b> condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereEqualStatement}
         */
        default WhereEqualStatement equal(Object o)
        {
            return create(new WhereEqualStatement("?"), o);
        }

        /**
         * Appends an <b>{@code EQUAL}</b> condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereEqualStatement}
         */
        default WhereEqualStatement equal(FinalStatement finalStatement)
        {
            return create(new WhereEqualStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}