package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT SUM aggregate function. Calculates the total sum of a numeric column.
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
     * Interface for containers that can create SELECT SUM statements.
     */
    public interface SelectSumStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT SUM statement for the specified column.
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