package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT AVG}</b> aggregate function. Calculates the average value of a numeric column.
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
     * Interface for containers that can create <b>{@code SELECT AVG}</b> statements.
     */
    public interface SelectAvgStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT AVG}</b> statement for the specified column.
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
