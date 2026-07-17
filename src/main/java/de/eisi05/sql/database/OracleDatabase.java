package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

/**
 * A database configuration implementation targeted for Oracle database systems.
 */
public class OracleDatabase extends Database
{
    private final double version;

    /**
     * Constructs an Oracle database connection configuration using specific connection details.
     *
     * @param host     the database server host
     * @param port     the port number the Oracle listener is on
     * @param database the Oracle SID or Service Name
     * @param user     the database username
     * @param password the database password
     * @param version  the target Oracle database version
     */
    public OracleDatabase(String host, int port, String database, String user, String password, double version)
    {
        super("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@" + host + ":" + port + ":" + database, user, password, DatabaseType.ORACLE);
        this.version = version;
    }

    /**
     * Constructs an Oracle database connection configuration using a raw thin URL.
     *
     * @param url     the fully configured Oracle JDBC thin connection URL
     * @param version the target Oracle database version
     */
    public OracleDatabase(String url, double version)
    {
        super("oracle.jdbc.OracleDriver", url, null, null, DatabaseType.ORACLE);
        this.version = version;
    }

    /**
     * Gets the configured Oracle database version.
     *
     * @return the Oracle database version
     */
    public double getVersion()
    {
        return version;
    }
}