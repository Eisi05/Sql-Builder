package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class TruncateTableStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected TruncateTableStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "TRUNCATE TABLE";
    }

    public interface TruncateTableStatementContainer extends AbstractStatement.StatementContainer
    {
        default TruncateTableStatement truncateTable(String table)
        {
            return create(new TruncateTableStatement(table));
        }
    }
}
