package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL negation boundary condition fragment: <b>{@code NOT BETWEEN}</b> .
 */
public class WhereNotBetweenStatement extends WhereBetweenStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotBetweenStatement} with the given expression fragment boundaries.
     *
     * @param query the SQL placeholder boundaries expression
     */
    WhereNotBetweenStatement(String query)
    {
        super(query);
    }

    /**
     * Defines whether the keyword <b>{@code NOT}</b> should immediately follow the word <b>{@code WHERE}</b> structurally.
     *
     * @return {@code false} since <b>{@code NOT}</b> is grouped before <b>{@code BETWEEN}</b> rather than standalone
     */
    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code NOT BETWEEN}</b> clause creation.
     */
    public interface WhereNotBetweenStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code NOT BETWEEN}</b> condition targeting two positional object values.
         *
         * @param <T> the type of the boundary values
         * @param t1  the start range boundary
         * @param t2  the end range boundary
         * @return a configured {@link WhereNotBetweenStatement}
         */
        default <T> WhereNotBetweenStatement notBetween(T t1, T t2)
        {
            return create(new WhereNotBetweenStatement("? AND ?"), t1, t2);
        }

        /**
         * Appends a <b>{@code NOT BETWEEN}</b> condition where both boundaries are evaluated via subqueries or statement fragments.
         *
         * @param finalStatement1 the statement representing the start boundary
         * @param finalStatement2 the statement representing the end boundary
         * @return a configured {@link WhereNotBetweenStatement}
         */
        default WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        /**
         * Appends a <b>{@code NOT BETWEEN}</b> condition where the start boundary is a statement fragment and the end boundary is an object value.
         *
         * @param <T>             the type of the end boundary value
         * @param finalStatement1 the statement representing the start boundary
         * @param t2              the end boundary value parameter
         * @return a configured {@link WhereNotBetweenStatement}
         */
        default <T> WhereNotBetweenStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            return create(new WhereNotBetweenStatement("(" + finalStatement1.getQuery() + ") AND ?"), t2);
        }
    }
}