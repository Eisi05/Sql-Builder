package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.statements.AbstractStatement;

public class AlterTableStatement extends AbstractStatement implements AddColumnStatement.AddColumnStatementContainer,
        DropColumnStatement.DropColumnStatementContainer, RenameColumnStatement.RenameColumnStatementContainer,
        ModifyColumnStatement.ModifyColumnStatementContainer, AlterAutoIncrement.AlterAutoIncrementContainer,
        DropIndexStatement.DropIndexStatementContainer, ExecuteQueryStatement
{
    protected AlterTableStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "ALTER TABLE";
    }

    public interface AlterTableStatementContainer extends StatementContainer
    {
        default AlterTableStatement alterTable(String table)
        {
            return create(new AlterTableStatement(table));
        }
    }
}
