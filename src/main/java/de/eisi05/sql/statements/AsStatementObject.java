package de.eisi05.sql.statements;

public class AsStatementObject
{
    private final String key;

    private AsStatementObject(String key)
    {
        this.key = key;
    }

    public static AsStatementObject of(String key)
    {
        return new AsStatementObject(key);
    }

    public String getKey()
    {
        return key;
    }

    public FinalAsStatementObject as(String as)
    {
        if(as.contains(" "))
            as = "[" + as + "]";

        return new FinalAsStatementObject(key, as);
    }

    public record FinalAsStatementObject(String key, String as)
    {
    }
}
