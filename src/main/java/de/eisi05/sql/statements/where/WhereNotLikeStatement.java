package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL {@code NOT LIKE} pattern matching condition fragment.
 */
public class WhereNotLikeStatement extends WhereLikeStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotLikeStatement} with the given query pattern expression.
     *
     * @param query the SQL pattern expression
     */
    protected WhereNotLikeStatement(String query)
    {
        super(query);
    }

    /**
     * Defines whether the keyword NOT should immediately follow the word WHERE structurally.
     *
     * @return {@code false} since NOT is grouped directly within the {@code NOT LIKE} phrase
     */
    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    /**
     * A container interface providing fluent builder methods for {@code NOT LIKE} pattern clauses.
     */
    public interface WhereNotLikeStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a negated pattern matching comparison condition using an object value parameter.
         *
         * @param o the pattern object to match against
         * @return a configured {@link WhereNotLikeStatement}
         */
        default WhereNotLikeStatement notLike(Object o)
        {
            return create(new WhereNotLikeStatement("?"), o);
        }

        /**
         * Appends a negated pattern matching comparison condition evaluating against an SQL subquery or expression fragment.
         *
         * @param finalStatement the statement yielding the pattern expression
         * @return a configured {@link WhereNotLikeStatement}
         */
        default WhereNotLikeStatement notLike(FinalStatement finalStatement)
        {
            return create(new WhereNotLikeStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}