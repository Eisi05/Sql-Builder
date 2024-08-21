package de.eisi05.sql.database;

import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.exceptions.UnableToConnectException;
import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.DatabaseStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Database implements AbstractStatement.StatementContainer
{
    private final String className;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final DatabaseType databaseType;

    protected Connection con;

    protected Database(String className, String connectionUrl, String username, String password,
                       DatabaseType databaseType)
    {
        this.className = className;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
    }

    public DatabaseType getDatabaseType()
    {
        return databaseType;
    }

    public SQLData connect()
    {
        try
        {
            Class.forName(className);
            if(username == null && password == null)
                con = DriverManager.getConnection(connectionUrl);
            else
                con = DriverManager.getConnection(connectionUrl, username, password);
        } catch(Exception e)
        {
            throw new UnableToConnectException(e);
        }
        return create(new SQLData(this));
    }

    public static class SQLData extends DatabaseStatement
    {
        public SQLData(Database database)
        {
            super(database, database.con);
        }

        public void close()
        {
            try
            {
                if(database.con != null)
                {
                    database.con.close();
                    database.con = null;
                }
            } catch(SQLException e)
            {
                throw new ConnectionIsClosedException(e);
            }
        }

        public boolean isConnected()
        {
            return database.con != null;
        }
    }
}
