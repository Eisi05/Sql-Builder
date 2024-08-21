package de.eisi05.sql.database;

import de.eisi05.sql.enums.DatabaseType;

public class MsAccessDatabase extends Database
{
    public MsAccessDatabase(String pathToDatabase)
    {
        super("net.ucanaccess.jdbc.UcanaccessDriver", "jdbc:ucanaccess://" + pathToDatabase,
                null, null, DatabaseType.MS_ACCESS);
    }
}
