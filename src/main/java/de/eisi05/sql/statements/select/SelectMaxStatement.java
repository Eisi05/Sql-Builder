package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT MAX}</b> aggregate function. Calculates the maximum value of a column.
 */
public class SelectMaxStatement extends SelectStatement
{
    /**
     * Constructs a new SelectMaxStatement for the specified column.
     *
     * @param key the column to calculate the maximum for
     */
    public SelectMaxStatement(String key)
    {
        super("MAX(" + key + ")");
    }

    /**
     * Interface for containers that can create <b>{@code SELECT MAX}</b> statements.
     */
    public interface SelectMaxStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT MAX}</b> statement for the specified column.
         *
         * @param key the column to calculate the maximum for
         * @return a new SelectMaxStatement
         */
        default SelectMaxStatement selectMax(String key)
        {
            return create(new SelectMaxStatement(key));
        }
    }
}