package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT AVG aggregate function. Calculates the average value of a numeric column.
 */
public class SelectAvgStatement extends SelectStatement
{
    /**
     * Constructs a new SelectAvgStatement for the specified column.
     *
     * @param key the column to calculate the average for
     */
    private SelectAvgStatement(String key)
    {
        super("AVG(" + key + ")");
    }

    /**
     * Interface for containers that can create SELECT AVG statements.
     */
    public interface SelectAvgStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT AVG statement for the specified column.
         *
         * @param key the column to calculate the average for
         * @return a new SelectAvgStatement
         */
        default SelectAvgStatement selectAvg(String key)
        {
            return create(new SelectAvgStatement(key));
        }
    }
}
