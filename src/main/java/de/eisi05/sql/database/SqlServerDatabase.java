package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

/**
 * A database configuration implementation targeted for Microsoft SQL Server database systems.
 */
public class SqlServerDatabase extends Database
{
    /**
     * Constructs a SQL Server connection configuration using host, port, and authentication details.
     *
     * @param host     the database server host
     * @param port     the port number the SQL Server service is listening on
     * @param database the database name to connect to
     * @param user     the database username
     * @param password the database password
     */
    public SqlServerDatabase(String host, int port, String database, String user, String password)
    {
        super("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database, user, password,
                DatabaseType.SQL_SERVER);
    }

    /**
     * Constructs a SQL Server connection configuration using a raw JDBC connection URL.
     *
     * @param url the fully configured JDBC connection URL
     */
    public SqlServerDatabase(String url)
    {
        super("com.microsoft.sqlserver.jdbc.SQLServerDriver", url, null, null, DatabaseType.SQL_SERVER);
    }
}