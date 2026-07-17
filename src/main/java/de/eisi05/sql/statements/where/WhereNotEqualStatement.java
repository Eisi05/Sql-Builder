package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL non-equality condition fragment.
 */
public class WhereNotEqualStatement extends WhereEqualStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotEqualStatement} with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereNotEqualStatement(String query)
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
     * A container interface providing fluent builder methods for non-equality conditions.
     */
    public interface WhereNotEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a non-equality condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereNotEqualStatement}
         */
        default WhereNotEqualStatement notEqual(Object o)
        {
            return create(new WhereNotEqualStatement("?"), o);
        }

        /**
         * Appends a non-equality condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereNotEqualStatement}
         */
        default WhereNotEqualStatement notEqual(FinalStatement finalStatement)
        {
            return create(new WhereNotEqualStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}