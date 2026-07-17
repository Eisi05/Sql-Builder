package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL ALTER TABLE statement used to add, drop, rename, or modify table structures.
 */
public class AlterTableStatement extends AbstractStatement implements AddColumnStatement.AddColumnStatementContainer,
                                                                      DropColumnStatement.DropColumnStatementContainer,
                                                                      RenameColumnStatement.RenameColumnStatementContainer,
                                                                      ModifyColumnStatement.ModifyColumnStatementContainer,
                                                                      AlterAutoIncrement.AlterAutoIncrementContainer,
                                                                      DropIndexStatement.DropIndexStatementContainer, ExecuteQueryStatement
{
    /**
     * Constructs an AlterTableStatement targeting the specified table.
     *
     * @param table the target table name to alter
     */
    protected AlterTableStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "ALTER TABLE"
     */
    @Override
    protected String getKey()
    {
        return "ALTER TABLE";
    }

    /**
     * Interface for containers that can initiate alter table sequences.
     */
    public interface AlterTableStatementContainer extends StatementContainer
    {
        /**
         * Starts an ALTER TABLE block on the specified target table.
         *
         * @param table the table name to modify
         * @return a new AlterTableStatement builder
         */
        default AlterTableStatement alterTable(String table)
        {
            return create(new AlterTableStatement(table));
        }
    }
}