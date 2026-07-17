package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL <b>{@code NOT LESS THAN}</b> condition fragment.
 */
public class WhereNotLessThanStatement extends WhereLessThanStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotLessThanStatement} with the given query expression.
     *
     * @param query the SQL query target expression
     */
    protected WhereNotLessThanStatement(String query)
    {
        super(query);
    }

    /**
     * Defines whether the keyword <b>{@code NOT}</b> should immediately follow the word <b>{@code WHERE}</b> structurally.
     *
     * @return {@code true} indicating a standalone <b>{@code NOT}</b> rule placement modification
     */
    @Override
    public boolean isNotAfterWhere()
    {
        return true;
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code NOT LESS THAN}</b> conditions.
     */
    public interface WhereNotLessStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code NOT LESS THAN}</b> condition against a single object value parameter.
         *
         * @param o the object value to compare against
         * @return a configured {@link WhereNotLessThanStatement}
         */
        default WhereNotLessThanStatement notLess(Object o)
        {
            return create(new WhereNotLessThanStatement("?"), o);
        }

        /**
         * Appends a <b>{@code NOT LESS THAN}</b> condition evaluating against an SQL subquery or statement fragment.
         *
         * @param finalStatement the statement representing the target value
         * @return a configured {@link WhereNotLessThanStatement}
         */
        default WhereNotLessThanStatement notLess(FinalStatement finalStatement)
        {
            return create(new WhereNotLessThanStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}