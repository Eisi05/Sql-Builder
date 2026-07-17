package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT COUNT aggregate function. Calculates the number of rows that match specified criteria.
 */
public class SelectCountStatement extends SelectStatement
{
    /**
     * Constructs a new SelectCountStatement for the specified column or expression.
     *
     * @param key the column or expression to count
     */
    private SelectCountStatement(String key)
    {
        super("COUNT(" + key + ")");
    }

    /**
     * Interface for containers that can create SELECT COUNT statements.
     */
    public interface SelectCountStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT COUNT statement for the specified column or expression.
         *
         * @param key the column or expression to count
         * @return a new SelectCountStatement
         */
        default SelectCountStatement selectCount(String key)
        {
            return create(new SelectCountStatement(key));
        }

        /**
         * Creates a SELECT COUNT DISTINCT statement for the specified column.
         *
         * @param key the column to count unique values for
         * @return a new SelectCountStatement containing DISTINCT
         */
        default SelectCountStatement selectCountDistinct(String key)
        {
            return create(new SelectCountStatement("DISTINCT " + key));
        }
    }
}