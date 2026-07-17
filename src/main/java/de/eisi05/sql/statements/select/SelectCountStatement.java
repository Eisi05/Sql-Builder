package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT COUNT}</b> aggregate function. Calculates the number of rows that match specified criteria.
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
     * Interface for containers that can create <b>{@code SELECT COUNT}</b> statements.
     */
    public interface SelectCountStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT COUNT}</b> statement for the specified column or expression.
         *
         * @param key the column or expression to count
         * @return a new SelectCountStatement
         */
        default SelectCountStatement selectCount(String key)
        {
            return create(new SelectCountStatement(key));
        }

        /**
         * Creates a <b>{@code SELECT COUNT DISTINCT}</b> statement for the specified column.
         *
         * @param key the column to count unique values for
         * @return a new SelectCountStatement containing <b>{@code DISTINCT}</b>
         */
        default SelectCountStatement selectCountDistinct(String key)
        {
            return create(new SelectCountStatement("DISTINCT " + key));
        }
    }
}