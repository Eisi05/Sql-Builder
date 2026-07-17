package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL <b>{@code ON}</b> clause used in conjunction with <b>{@code CREATE INDEX}</b> statements to define target tables and columns.
 */
public class OnIndexStatement extends FinalStatement
{
    /**
     * Constructs a new OnIndexStatement with the given table and column specifications.
     *
     * @param query the partial query indicating target tables and their columns
     */
    protected OnIndexStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement block.
     *
     * @return "ON"
     */
    @Override
    protected String getKey()
    {
        return "ON";
    }

    /**
     * Interface for containers that can append <b>{@code ON}</b> targets to index creation routines.
     */
    public interface OnIndexStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Targets a specific table and columns for the index configuration.
         *
         * @param table   the target table name
         * @param columns the array of columns to include in the index
         * @return a new OnIndexStatement mapping columns to the table
         */
        default OnIndexStatement on(String table, String... columns)
        {
            return create(new OnIndexStatement(table + " (" + String.join(", ", columns) + ")"));
        }
    }
}