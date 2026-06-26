package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

public class PostgresDatabase extends Database
{
    public PostgresDatabase(String host, int port, String database, String user, String password)
    {
        super("org.postgresql.Driver", "jdbc:postgresql://" + host + ":" + port + "/" + database, user, password, DatabaseType.POSTGRESQL);
    }

    public PostgresDatabase(String connectionUrl)
    {
        super("org.postgresql.Driver", connectionUrl, null, null, DatabaseType.POSTGRESQL);
    }
}
