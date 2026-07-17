package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT SUM}</b> aggregate function. Calculates the total sum of a numeric column.
 */
public class SelectSumStatement extends SelectStatement
{
    /**
     * Constructs a new SelectSumStatement for the specified column.
     *
     * @param key the column to calculate the sum for
     */
    private SelectSumStatement(String key)
    {
        super("SUM(" + key + ")");
    }

    /**
     * Interface for containers that can create <b>{@code SELECT SUM}</b> statements.
     */
    public interface SelectSumStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT SUM}</b> statement for the specified column.
         *
         * @param key the column to calculate the sum for
         * @return a new SelectSumStatement
         */
        default SelectSumStatement selectSum(String key)
        {
            return create(new SelectSumStatement(key));
        }
    }
}