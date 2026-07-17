package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL greater-than condition ({@code >}) fragment.
 */
public class WhereGreaterThanStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereGreaterThanStatement} with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereGreaterThanStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL operator for greater than.
     *
     * @return {@code ">"}
     */
    @Override
    protected String getKey()
    {
        return ">";
    }

    /**
     * A container interface providing fluent builder methods for greater-than conditions.
     */
    public interface WhereGreaterThanStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a greater-than condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereGreaterThanStatement}
         */
        default WhereGreaterThanStatement greaterThan(Object o)
        {
            return create(new WhereGreaterThanStatement("?"), o);
        }

        /**
         * Appends a greater-than condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereGreaterThanStatement}
         */
        default WhereGreaterThanStatement greaterThan(FinalStatement finalStatement)
        {
            return create(new WhereGreaterThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}