package de.eisi05.sql.statements;

import de.eisi05.sql.database.Database;

import java.sql.Connection;

public abstract class DatabaseStatement extends AbstractStatement
        implements AbstractStatement.DefaultStatementContainers
{
    protected final Database database;
    protected final Connection connection;

    protected DatabaseStatement(Database database, Connection connection)
    {
        super(null);
        this.database = database;
        this.connection = connection;
    }

    public Connection getConnection()
    {
        return connection;
    }

    @Override
    protected String getKey()
    {
        return "";
    }
}
