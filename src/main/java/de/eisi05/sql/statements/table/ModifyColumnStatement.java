package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL MODIFY or ALTER COLUMN statement within alter table contexts.
 */
public class ModifyColumnStatement extends FinalStatement
{
    private final String key;

    /**
     * Constructs a new ModifyColumnStatement.
     *
     * @param query the modified column specification
     * @param key   the action keyword (e.g. "MODIFY" or "ALTER")
     */
    protected ModifyColumnStatement(String query, String key)
    {
        super(query);
        this.key = key;
    }

    /**
     * Gets the dynamic keyword corresponding to this table modification action.
     *
     * @return the resolved modification keyword (e.g., "MODIFY COLUMN" or "ALTER COLUMN")
     */
    @Override
    protected String getKey()
    {
        return key + " COLUMN";
    }

    /**
     * Interface for containers that can modify or alter table columns.
     */
    public interface ModifyColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Formulates a "MODIFY COLUMN" action mapping to SQL specifications.
         *
         * @param key      the column to modify
         * @param dataType the target SqlDataType
         * @return a new ModifyColumnStatement using "MODIFY" keyword structures
         */
        default ModifyColumnStatement modify(String key, SqlDataType<?> dataType)
        {
            return create(new ModifyColumnStatement(key + " " + dataType.getName(), "MODIFY"));
        }

        /**
         * Formulates an "ALTER COLUMN" action mapping to alternate SQL dialects.
         *
         * @param key      the column to alter
         * @param dataType the target SqlDataType
         * @return a new ModifyColumnStatement using "ALTER" keyword structures
         */
        default ModifyColumnStatement alter(String key, SqlDataType<?> dataType)
        {
            return create(new ModifyColumnStatement(key + " " + dataType.getName(), "ALTER"));
        }
    }
}