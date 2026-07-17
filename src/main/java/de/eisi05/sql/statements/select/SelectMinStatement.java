package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT MIN aggregate function. Calculates the minimum value of a column.
 */
public class SelectMinStatement extends SelectStatement
{
    /**
     * Constructs a new SelectMinStatement for the specified column.
     *
     * @param key the column to calculate the minimum for
     */
    private SelectMinStatement(String key)
    {
        super("MIN(" + key + ")");
    }

    /**
     * Interface for containers that can create SELECT MIN statements.
     */
    public interface SelectMinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT MIN statement for the specified column.
         *
         * @param key the column to calculate the minimum for
         * @return a new SelectMinStatement
         */
        default SelectMinStatement selectMin(String key)
        {
            return create(new SelectMinStatement(key));
        }
    }
}