package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents an SQL <b>{@code IS NOT NULL}</b> condition fragment.
 */
public class WhereNotIsNullStatement extends WhereIsNullStatement
{
    /**
     * Constructs a {@code WhereNotIsNullStatement} with the given query expression.
     *
     * @param query the SQL fragment expression
     */
    protected WhereNotIsNullStatement(String query)
    {
        super(query);
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code IS NOT NULL}</b> clauses.
     */
    public interface WhereNotIsNullStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an <b>{@code IS NOT NULL}</b> condition.
         *
         * @return a configured {@link WhereNotIsNullStatement} targeting non-null entries
         */
        default WhereNotIsNullStatement isNotNull()
        {
            return create(new WhereNotIsNullStatement("NOT NULL"));
        }
    }
}