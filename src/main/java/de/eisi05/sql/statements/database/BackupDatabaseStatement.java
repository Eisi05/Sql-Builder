package de.eisi05.sql.statements.database;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

public class BackupDatabaseStatement extends AbstractStatement implements ToDiskStatement.ToDiskStatementContainer,
        ExecuteUpdateStatement
{
    protected BackupDatabaseStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "BACKUP DATABASE";
    }

    public interface BackupDatabaseStatementContainer extends StatementContainer
    {
        default BackupDatabaseStatement backupDatabase(String name)
        {
            return create(new BackupDatabaseStatement(name));
        }
    }
}

