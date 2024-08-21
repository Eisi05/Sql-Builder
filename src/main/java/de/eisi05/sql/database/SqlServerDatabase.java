package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

public class SqlServerDatabase extends Database
{
    public SqlServerDatabase(String host, int port, String database, String user, String password)
    {
        super("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database, user, password,
                DatabaseType.SQL_SERVER);
    }
}
