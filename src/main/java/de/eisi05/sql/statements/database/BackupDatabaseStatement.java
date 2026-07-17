package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code BACKUP DATABASE}</b> statement. Used to create a backup of a database to a disk location. SQL Server-specific syntax.
 */
public class BackupDatabaseStatement extends AbstractStatement implements ToDiskStatement.ToDiskStatementContainer,
                                                                          ExecuteUpdateStatement
{
    /**
     * Constructs a new BackupDatabaseStatement for the specified database.
     *
     * @param name the database name to backup
     */
    protected BackupDatabaseStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "BACKUP DATABASE"
     */
    @Override
    protected String getKey()
    {
        return "BACKUP DATABASE";
    }

    /**
     * Interface for containers that can create <b>{@code BACKUP DATABASE}</b> statements.
     */
    public interface BackupDatabaseStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code BACKUP DATABASE}</b> statement for the specified database.
         *
         * @param name the database name to backup
         * @return a new BackupDatabaseStatement
         */
        default BackupDatabaseStatement backupDatabase(String name)
        {
            return create(new BackupDatabaseStatement(name));
        }
    }
}
