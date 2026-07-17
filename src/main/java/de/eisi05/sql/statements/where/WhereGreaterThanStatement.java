package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL <b>{@code GREATER THAN}</b> condition ({@code >}) fragment.
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
     * Returns the SQL operator for <b>{@code GREATER THAN}</b> .
     *
     * @return {@code ">"}
     */
    @Override
    protected String getKey()
    {
        return ">";
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code GREATER THAN}</b> conditions.
     */
    public interface WhereGreaterThanStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code GREATER THAN}</b> condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereGreaterThanStatement}
         */
        default WhereGreaterThanStatement greaterThan(Object o)
        {
            return create(new WhereGreaterThanStatement("?"), o);
        }

        /**
         * Appends a <b>{@code GREATER THAN}</b> condition evaluating against an SQL subquery or statement fragment.
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