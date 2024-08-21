package de.eisi05.sql.interfaces;

public interface SqlFunctions
{
    static String ifNull(String value, Object defaultValue)
    {
        //TODO: Check for MySql

        if(defaultValue instanceof String)
            defaultValue = "'" + defaultValue + "'";

        return "IFNULL(" + value + ", " + defaultValue + ")";
    }

    static String coalesce(String value, Object defaultValue)
    {
        //TODO: Check for MySql and Oracle

        if(defaultValue instanceof String)
            defaultValue = "'" + defaultValue + "'";

        return "COALESCE(" + value + ", " + defaultValue + ")";
    }

    static String isNull(String value, Object defaultValue)
    {
        //TODO: Check for Ms Access

        if(defaultValue instanceof String)
            defaultValue = "'" + defaultValue + "'";

        return "ISNULL(" + value + ", " + defaultValue + ")";
    }

    static String nvl(String value, Object defaultValue)
    {
        //TODO: Check for Oracle

        if(defaultValue instanceof String)
            defaultValue = "'" + defaultValue + "'";

        return "NVL(" + value + ", " + defaultValue + ")";
    }
}
