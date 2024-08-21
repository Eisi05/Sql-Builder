package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

public class DropDatabaseStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected DropDatabaseStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "DROP DATABASE";
    }

    public interface DropDatabaseStatementContainer extends StatementContainer
    {
        default DropDatabaseStatement dropDatabase(String name)
        {
            return create(new DropDatabaseStatement(name));
        }
    }
}
