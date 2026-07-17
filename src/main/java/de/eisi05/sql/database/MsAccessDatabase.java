package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

/**
 * A database configuration implementation targeted for MS Access databases using Ucanaccess.
 */
public class MsAccessDatabase extends Database
{
    /**
     * Constructs a new Microsoft Access database connection configuration.
     *
     * @param pathToDatabase the file path to the MS Access database file (e.g., .accdb or .mdb file)
     */
    public MsAccessDatabase(String pathToDatabase)
    {
        super("net.ucanaccess.jdbc.UcanaccessDriver", "jdbc:ucanaccess://" + pathToDatabase, null, null, DatabaseType.MS_ACCESS);
    }
}