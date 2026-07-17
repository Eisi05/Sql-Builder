package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL DROP TABLE statement used to delete an entire table structure from a database.
 */
public class DropTableStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new DropTableStatement for the specified table.
     *
     * @param table the target table to drop
     */
    protected DropTableStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "DROP TABLE"
     */
    @Override
    protected String getKey()
    {
        return "DROP TABLE";
    }

    /**
     * Interface for containers that can execute table-drop sequences.
     */
    public interface DropTableStatementContainer extends StatementContainer
    {
        /**
         * Destroys the specified table structure.
         *
         * @param table the table to drop
         * @return a new DropTableStatement
         */
        default DropTableStatement dropTable(String table)
        {
            return create(new DropTableStatement(table));
        }
    }
}