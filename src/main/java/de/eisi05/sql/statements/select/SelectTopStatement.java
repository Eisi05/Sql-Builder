package de.eisi05.sql.statements.select;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

/**
 * Represents a SQL <b>{@code SELECT TOP}</b> statement. Specifies the number of records to return. Typically supported by specific engines like SQL Server and
 * MS Access.
 */
@SqlData({DatabaseType.SQL_SERVER, DatabaseType.MS_ACCESS})
public class SelectTopStatement extends SelectStatement
{
    /**
     * Constructs a new SelectTopStatement with the specified raw query snippet.
     *
     * @param query the partial query string representing the top criteria and columns
     */
    private SelectTopStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "SELECT TOP"
     */
    @Override
    protected String getKey()
    {
        return "SELECT TOP";
    }

    /**
     * Interface for containers that can create <b>{@code SELECT TOP}</b> statements.
     */
    public interface SelectTopStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code SELECT TOP}</b> statement with a fixed maximum number of columns.
         *
         * @param amount the absolute number of rows to return
         * @param keys   the columns to select
         * @return a new SelectTopStatement
         */
        default SelectTopStatement selectTop(int amount, String... keys)
        {
            return create(new SelectTopStatement(amount + " " + String.join(", ", keys)));
        }

        /**
         * Creates a <b>{@code SELECT TOP PERCENT}</b> statement returning a percentage of the total record set.
         *
         * @param percent the percentage of rows to return (0-100)
         * @param keys    the columns to select
         * @return a new SelectTopStatement with <b>{@code PERCENT}</b> modifier
         */
        default SelectTopStatement selectTopPercent(int percent, String... keys)
        {
            return create(new SelectTopStatement(percent + " PERCENT " + String.join(", ", keys)));
        }
    }
}