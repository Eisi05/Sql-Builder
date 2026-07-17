package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL CREATE DATABASE statement. Creates a new database with the specified name.
 */
public class CreateDatabaseStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new CreateDatabaseStatement for the specified database.
     *
     * @param name the database name
     */
    protected CreateDatabaseStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "CREATE DATABASE"
     */
    @Override
    protected String getKey()
    {
        return "CREATE DATABASE";
    }

    /**
     * Interface for containers that can create CREATE DATABASE statements.
     */
    public interface CreateDataBaseStatementContainer extends StatementContainer
    {
        /**
         * Creates a CREATE DATABASE statement for the specified database.
         *
         * @param name the database name
         * @return a new CreateDatabaseStatement
         */
        default CreateDatabaseStatement createDatabase(String name)
        {
            return create(new CreateDatabaseStatement(name));
        }
    }
}
