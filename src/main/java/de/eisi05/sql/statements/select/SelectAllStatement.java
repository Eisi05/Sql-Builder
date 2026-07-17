package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL SELECT ALL statement. Selects all rows including duplicates (default behavior). Equivalent to SELECT without DISTINCT.
 */
public class SelectAllStatement extends SelectStatement
{
    /**
     * Constructs a new SelectAllStatement with the specified columns.
     *
     * @param keys the columns to select
     */
    private SelectAllStatement(String... keys)
    {
        super(String.join(", ", keys));
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "SELECT ALL"
     */
    @Override
    protected String getKey()
    {
        return "SELECT ALL";
    }

    /**
     * Interface for containers that can create SELECT ALL statements.
     */
    public interface SelectAllStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a SELECT ALL statement with the specified columns.
         *
         * @param keys the columns to select
         * @return a new SelectAllStatement
         */
        default SelectAllStatement selectAll(String... keys)
        {
            return create(new SelectAllStatement(keys));
        }
    }
}
