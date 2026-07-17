package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT DISTINCT statement. Returns only distinct (different) values.
 */
public class SelectDistinctStatement extends SelectStatement
{
    /**
     * Constructs a new SelectDistinctStatement with the specified columns.
     *
     * @param keys the columns to select distinct values from
     */
    private SelectDistinctStatement(String... keys)
    {
        super(keys);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "SELECT DISTINCT"
     */
    @Override
    protected String getKey()
    {
        return "SELECT DISTINCT";
    }

    /**
     * Interface for containers that can create SELECT DISTINCT statements.
     */
    public interface SelectDistinctStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT DISTINCT statement with the specified columns.
         *
         * @param keys the columns to select distinct values from
         * @return a new SelectDistinctStatement
         */
        default SelectDistinctStatement selectDistinct(String... keys)
        {
            return create(new SelectDistinctStatement(keys));
        }
    }
}