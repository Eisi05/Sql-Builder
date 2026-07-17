package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL {@code LIKE} pattern matching condition fragment.
 */
public class WhereLikeStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereLikeStatement} with the given query pattern expression.
     *
     * @param query the SQL pattern expression
     */
    protected WhereLikeStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL pattern-matching operator keyword.
     *
     * @return {@code "LIKE"}
     */
    @Override
    protected String getKey()
    {
        return "LIKE";
    }

    /**
     * A container interface providing fluent builder methods for {@code LIKE} pattern clauses.
     */
    public interface WhereLikeStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a pattern matching comparison condition using an object value parameter.
         *
         * @param o the pattern object (typically a String with wildcards like %) to match against
         * @return a configured {@link WhereLikeStatement}
         */
        default WhereLikeStatement like(Object o)
        {
            return create(new WhereLikeStatement("?"), o);
        }

        /**
         * Appends a pattern matching comparison condition evaluating against an SQL subquery or expression fragment.
         *
         * @param finalStatement the statement yielding the pattern expression
         * @return a configured {@link WhereLikeStatement}
         */
        default WhereLikeStatement like(FinalStatement finalStatement)
        {
            return create(new WhereLikeStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}