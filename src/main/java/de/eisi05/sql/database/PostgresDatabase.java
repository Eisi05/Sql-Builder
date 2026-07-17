package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

/**
 * A database configuration implementation targeted for PostgreSQL database systems.
 */
public class PostgresDatabase extends Database
{
    /**
     * Constructs a PostgreSQL connection configuration using host, port, and authentication details.
     *
     * @param host     the database server host
     * @param port     the port number the PostgreSQL service is listening on
     * @param database the database name to connect to
     * @param user     the database username
     * @param password the database password
     */
    public PostgresDatabase(String host, int port, String database, String user, String password)
    {
        super("org.postgresql.Driver", "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password, DatabaseType.POSTGRESQL);
    }

    /**
     * Constructs a PostgreSQL connection configuration using a raw JDBC connection URL.
     *
     * @param connectionUrl the fully configured JDBC connection URL
     */
    public PostgresDatabase(String connectionUrl)
    {
        super("org.postgresql.Driver", connectionUrl, null, null, DatabaseType.POSTGRESQL);
    }
}