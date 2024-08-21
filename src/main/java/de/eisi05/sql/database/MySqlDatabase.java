package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

public class MySqlDatabase extends Database
{
    public MySqlDatabase(String host, int port, String database, String user, String password)
    {
        super("com.mysql.cj.jdbc.Driver", "jdbc:mysql://" + host + ":" + port + "/" + database,
                user, password, DatabaseType.MYSQL);
    }
}
