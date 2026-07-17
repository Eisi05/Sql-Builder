package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL negated greater-than condition fragment.
 */
public class WhereNotGreaterThanStatement extends WhereGreaterThanStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotGreaterThanStatement} with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereNotGreaterThanStatement(String query)
    {
        super(query);
    }

    /**
     * Defines whether the keyword NOT should immediately follow the word WHERE structurally.
     *
     * @return {@code true} indicating a standalone NOT rule placement modification
     */
    @Override
    public boolean isNotAfterWhere()
    {
        return true;
    }

    /**
     * A container interface providing fluent builder methods for negated greater-than conditions.
     */
    public interface WhereNotGreaterStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a negated greater-than condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereNotGreaterThanStatement}
         */
        default WhereNotGreaterThanStatement notGreaterThan(Object o)
        {
            return create(new WhereNotGreaterThanStatement("?"), o);
        }

        /**
         * Appends a negated greater-than condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereNotGreaterThanStatement}
         */
        default WhereNotGreaterThanStatement notGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereNotGreaterThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}