package de.eisi05.sql.database;

import com.mysql.cj.exceptions.ConnectionIsClosedException;
import com.mysql.cj.exceptions.UnableToConnectException;
import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.DatabaseStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An abstract representation of a database connection configuration. Implementing classes define the connection details for specific database systems. It
 * serves as a {@link de.eisi05.sql.statements.AbstractStatement.StatementContainer} to support database operations.
 */
public abstract class Database implements AbstractStatement.StatementContainer
{
    private final String className;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final DatabaseType databaseType;

    /**
     * The active connection to the database.
     */
    protected Connection con;

    /**
     * Constructs a database configuration with connection details.
     *
     * @param className     the fully qualified name of the JDBC driver class
     * @param connectionUrl the JDBC connection URL
     * @param username      the database user name, or {@code null} if none
     * @param password      the database password, or {@code null} if none
     * @param databaseType  the type of database system being targeted
     */
    protected Database(String className, String connectionUrl, String username, String password, DatabaseType databaseType)
    {
        this.className = className;
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.databaseType = databaseType;
    }

    /**
     * Gets the type of database targeted by this configuration.
     *
     * @return the database type
     */
    public DatabaseType getDatabaseType()
    {
        return databaseType;
    }

    /**
     * Attempts to load the JDBC driver and establish a connection to the database.
     *
     * @return a new {@link SQLData} instance initialized with this connection
     * @throws UnableToConnectException if the driver cannot be loaded or the connection fails
     */
    public SQLData connect()
    {
        try
        {
            Class.forName(className);
            if(username == null && password == null)
                con = DriverManager.getConnection(connectionUrl);
            else
                con = DriverManager.getConnection(connectionUrl, username, password);
        }
        catch(Exception e)
        {
            throw new UnableToConnectException(e);
        }
        return create(new SQLData(this));
    }

    /**
     * Represents an active database connection context, providing access to database statements and managing connection states.
     */
    public static class SQLData extends DatabaseStatement
    {
        /**
         * Constructs an active SQL session container.
         *
         * @param database the database configuration instance holding the connection
         */
        public SQLData(Database database)
        {
            super(database, () -> database.con, (conn) -> {});
        }

        /**
         * Closes the underlying database connection and releases resources.
         *
         * @throws ConnectionIsClosedException if a database access error occurs during closing
         */
        public void close()
        {
            try
            {
                if(database.con != null)
                {
                    database.con.close();
                    database.con = null;
                }
            }
            catch(SQLException e)
            {
                throw new ConnectionIsClosedException(e);
            }
        }

        /**
         * Checks whether the underlying database connection is currently active (not null). Note that this only checks local state, not the physical
         * connection's validity.
         *
         * @return true if the connection is available, false otherwise
         */
        public boolean isConnected()
        {
            return database.con != null;
        }
    }
}