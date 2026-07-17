package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code SELECT ALL}</b> statement. Selects all rows including duplicates (default behavior). Equivalent to <b>{@code SELECT}</b> without
 * <b>{@code DISTINCT}</b> .
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
     * Interface for containers that can create <b>{@code SELECT ALL}</b> statements.
     */
    public interface SelectAllStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT ALL}</b> statement with the specified columns.
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
