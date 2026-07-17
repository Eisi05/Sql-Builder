package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents an SQL <b>{@code IS NULL}</b> condition fragment.
 */
public class WhereIsNullStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereIsNullStatement} with the given query expression.
     *
     * @param query the SQL fragment expression
     */
    protected WhereIsNullStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL comparison operator keyword.
     *
     * @return {@code "IS"}
     */
    @Override
    protected String getKey()
    {
        return "IS";
    }

    /**
     * A container interface providing fluent builder methods for <b>{@code IS NULL}</b> clauses.
     */
    public interface WhereIsNullStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an <b>{@code IS NULL}</b> condition.
         *
         * @return a configured {@link WhereIsNullStatement} targeting null entries
         */
        default WhereIsNullStatement isNull()
        {
            return create(new WhereIsNullStatement("NULL"));
        }
    }
}