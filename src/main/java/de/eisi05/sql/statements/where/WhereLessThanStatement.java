package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL less-than condition ({@code <}) fragment.
 */
public class WhereLessThanStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereLessThanStatement} with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereLessThanStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL operator for less than.
     *
     * @return {@code "<"}
     */
    @Override
    protected String getKey()
    {
        return "<";
    }

    /**
     * A container interface providing fluent builder methods for less-than conditions.
     */
    public interface WhereLessThanStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a less-than condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereLessThanStatement}
         */
        default WhereLessThanStatement lessThan(Object o)
        {
            return create(new WhereLessThanStatement("?"), o);
        }

        /**
         * Appends a less-than condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereLessThanStatement}
         */
        default WhereLessThanStatement lessThan(FinalStatement finalStatement)
        {
            return create(new WhereLessThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}