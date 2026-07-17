package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL TRUNCATE TABLE statement to quickly delete all data records from a target table.
 */
public class TruncateTableStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new TruncateTableStatement against the specific target entity table.
     *
     * @param table the name of the target table to truncate
     */
    protected TruncateTableStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword representing this statement action.
     *
     * @return "TRUNCATE TABLE"
     */
    @Override
    protected String getKey()
    {
        return "TRUNCATE TABLE";
    }

    /**
     * Interface for containers that can execute quick table truncate workflows.
     */
    public interface TruncateTableStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Clears all structural records contained inside the targeting table database array.
         *
         * @param table the table identifier to clear records from
         * @return a new TruncateTableStatement execution map
         */
        default TruncateTableStatement truncateTable(String table)
        {
            return create(new TruncateTableStatement(table));
        }
    }
}