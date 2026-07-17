package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL <b>{@code BETWEEN}</b> condition fragment.
 * <p>
 * Example: {@code field BETWEEN ? AND ?}
 * </p>
 */
public class WhereBetweenStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereBetweenStatement} with the given query fragment.
     *
     * @param query the SQL placeholder expression for the <b>{@code BETWEEN}</b> boundaries
     */
    WhereBetweenStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL keyword for this statement.
     *
     * @return {@code "BETWEEN"}
     */
    @Override
    protected String getKey()
    {
        return "BETWEEN";
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code BETWEEN}</b> clause creation.
     */
    public interface WhereBetweenStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code BETWEEN}</b> condition targeting two positional object values.
         *
         * @param <T> the type of the boundary values
         * @param t1  the start boundary value
         * @param t2  the end boundary value
         * @return a configured {@link WhereBetweenStatement}
         */
        default <T> WhereBetweenStatement between(T t1, T t2)
        {
            return create(new WhereBetweenStatement("? AND ?"), t1, t2);
        }

        /**
         * Appends a <b>{@code BETWEEN}</b> condition where both boundaries are evaluated via subqueries or statement fragments.
         *
         * @param finalStatement1 the statement representing the start boundary
         * @param finalStatement2 the statement representing the end boundary
         * @return a configured {@link WhereBetweenStatement}
         */
        default WhereBetweenStatement between(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereBetweenStatement("(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        /**
         * Appends a <b>{@code BETWEEN}</b> condition where the start boundary is a statement fragment and the end boundary is an object value.
         *
         * @param <T>             the type of the end boundary value
         * @param finalStatement1 the statement representing the start boundary
         * @param t2              the end boundary value
         * @return a configured {@link WhereBetweenStatement}
         */
        default <T> WhereBetweenStatement between(FinalStatement finalStatement1, T t2)
        {
            return create(new WhereBetweenStatement("(" + finalStatement1.getQuery() + ") AND ?"), t2);
        }
    }
}