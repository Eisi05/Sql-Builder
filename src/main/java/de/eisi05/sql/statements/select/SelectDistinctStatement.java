package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT DISTINCT}</b> statement. Returns only distinct (different) values.
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
     * Interface for containers that can create <b>{@code SELECT DISTINCT}</b> statements.
     */
    public interface SelectDistinctStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT DISTINCT}</b> statement with the specified columns.
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