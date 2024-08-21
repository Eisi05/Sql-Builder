package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

public class OracleDatabase extends Database
{
    private final double version;

    public OracleDatabase(String host, int port, String database, String user, String password, double version)
    {
        super("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@" + host + ":" + port + ":" + database,
                user, password, DatabaseType.ORACLE);
        this.version = version;
    }

    public double getVersion()
    {
        return version;
    }
}
