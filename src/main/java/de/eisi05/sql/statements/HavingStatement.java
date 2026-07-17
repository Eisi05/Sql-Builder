package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereDefaultStatementContainer;

/**
 * Represents a SQL <b>{@code HAVING}</b> clause. Filters groups created by <b>{@code GROUP BY}</b> based on specified conditions. Supports aggregate functions
 * like <b>{@code COUNT}</b> in the filter condition.
 */
public class HavingStatement extends FinalStatement implements WhereDefaultStatementContainer,
                                                               ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new HavingStatement with the given condition.
     *
     * @param key the <b>{@code HAVING}</b> condition
     */
    protected HavingStatement(String key)
    {
        super(key);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "HAVING"
     */
    @Override
    protected String getKey()
    {
        return "HAVING";
    }

    /**
     * Interface for containers that can create <b>{@code HAVING}</b> statements.
     */
    public interface HavingStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code HAVING}</b> statement with a <b>{@code COUNT}</b> condition.
         *
         * @param key the column to count
         * @return a new HavingStatement with <b>{@code COUNT}</b> condition
         */
        default HavingStatement hasCount(String key)
        {
            return create(new HavingStatement("COUNT(" + key + ")"));
        }
    }
}
