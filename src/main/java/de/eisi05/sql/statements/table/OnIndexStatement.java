package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class OnIndexStatement extends FinalStatement
{
    protected OnIndexStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "ON";
    }

    public interface OnIndexStatementContainer extends AbstractStatement.StatementContainer
    {
        default OnIndexStatement on(String table, String... columns)
        {
            return create(new OnIndexStatement(table + " (" + String.join(", ", columns) + ")"));
        }
    }
}
