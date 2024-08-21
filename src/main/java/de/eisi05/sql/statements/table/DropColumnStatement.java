package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class DropColumnStatement extends FinalStatement
{
    protected DropColumnStatement(String key)
    {
        super(key);
    }

    @Override
    protected String getKey()
    {
        return "DROP COLUMN";
    }

    public interface DropColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        default DropColumnStatement dropColumn(String key)
        {
            return create(new DropColumnStatement(key));
        }
    }
}
