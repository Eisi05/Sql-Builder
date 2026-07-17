package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL DROP COLUMN statement used during alter table routines.
 */
public class DropColumnStatement extends FinalStatement
{
    /**
     * Constructs a new DropColumnStatement for the specified column.
     *
     * @param key the target column name to remove
     */
    protected DropColumnStatement(String key)
    {
        super(key);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "DROP COLUMN"
     */
    @Override
    protected String getKey()
    {
        return "DROP COLUMN";
    }

    /**
     * Interface for containers that can execute drop column definitions.
     */
    public interface DropColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a DROP COLUMN routine targeting a specific column.
         *
         * @param key the column to drop
         * @return a new DropColumnStatement
         */
        default DropColumnStatement dropColumn(String key)
        {
            return create(new DropColumnStatement(key));
        }
    }
}