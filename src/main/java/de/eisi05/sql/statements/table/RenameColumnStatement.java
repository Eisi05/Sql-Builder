package de.eisi05.sql.statements.table;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL RENAME block within an ALTER TABLE flow to rename an existing column.
 */
@SqlData(value = {DatabaseType.MYSQL, DatabaseType.MS_ACCESS, DatabaseType.ORACLE}, oracleVersion = 10)
public class RenameColumnStatement extends FinalStatement
{
    /**
     * Constructs a new RenameColumnStatement with the specified transition fragment.
     *
     * @param query the structural renaming snippet representing the old to new transition
     */
    protected RenameColumnStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement action.
     *
     * @return "RENAME"
     */
    @Override
    protected String getKey()
    {
        return "RENAME";
    }

    /**
     * Interface for containers that can manage column renames within database alterations.
     */
    public interface RenameColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Alters a column's descriptor name to a target identifier.
         *
         * @param key the original column name
         * @param to  the new name mapping for the target column
         * @return a new RenameColumnStatement mapping the column rename query
         */
        default RenameColumnStatement renameColumn(String key, String to)
        {
            return create(new RenameColumnStatement(key + " TO " + to));
        }
    }
}