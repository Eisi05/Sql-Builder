package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class AlterAutoIncrement extends FinalStatement
{
    protected AlterAutoIncrement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "AUTO_INCREMENT=";
    }

    public interface AlterAutoIncrementContainer extends AbstractStatement.StatementContainer
    {
        default AlterAutoIncrement autoIncrement(int startIncrement)
        {
            return create(new AlterAutoIncrement(String.valueOf(startIncrement)));
        }
    }
}
