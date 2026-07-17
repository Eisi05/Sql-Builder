package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL DROP DATABASE statement. Deletes an existing database with the specified name.
 */
public class DropDatabaseStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new DropDatabaseStatement for the specified database.
     *
     * @param name the database name to drop
     */
    protected DropDatabaseStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "DROP DATABASE"
     */
    @Override
    protected String getKey()
    {
        return "DROP DATABASE";
    }

    /**
     * Interface for containers that can create DROP DATABASE statements.
     */
    public interface DropDatabaseStatementContainer extends StatementContainer
    {
        /**
         * Creates a DROP DATABASE statement for the specified database.
         *
         * @param name the database name to drop
         * @return a new DropDatabaseStatement
         */
        default DropDatabaseStatement dropDatabase(String name)
        {
            return create(new DropDatabaseStatement(name));
        }
    }
}
