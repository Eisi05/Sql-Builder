package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

/**
 * A database configuration implementation targeted for MySQL database systems.
 */
public class MySqlDatabase extends Database
{
    /**
     * Constructs a MySQL connection configuration using host, port, and authentication details.
     *
     * @param host     the database server host
     * @param port     the port number the MySQL service is listening on
     * @param database the database name to connect to
     * @param user     the database username
     * @param password the database password
     */
    public MySqlDatabase(String host, int port, String database, String user, String password)
    {
        super("com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + host + ":" + port + "/" + database, user, password, DatabaseType.MYSQL);
    }

    /**
     * Constructs a MySQL connection configuration using a raw JDBC connection URL.
     *
     * @param connectionUrl the fully configured JDBC connection URL
     */
    public MySqlDatabase(String connectionUrl)
    {
        super("com.mysql.cj.jdbc.Driver", connectionUrl, null, null, DatabaseType.MYSQL);
    }
}