package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

public class DropTableStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected DropTableStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "DROP TABLE";
    }

    public interface DropTableStatementContainer extends StatementContainer
    {
        default DropTableStatement dropTable(String table)
        {
            return create(new DropTableStatement(table));
        }
    }
}
