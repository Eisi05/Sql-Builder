package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

public class CreateDatabaseStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected CreateDatabaseStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "CREATE DATABASE";
    }

    public interface CreateDataBaseStatementContainer extends StatementContainer
    {
        default CreateDatabaseStatement createDatabase(String name)
        {
            return create(new CreateDatabaseStatement(name));
        }
    }
}
