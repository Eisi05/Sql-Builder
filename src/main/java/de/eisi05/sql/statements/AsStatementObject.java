package de.eisi05.sql.statements;

/**
 * Represents a SQL column or expression that can be aliased using the <b>{@code AS}</b> keyword. Used to create structured alias objects for <b>{@code SELECT}</b> statements and other
 * contexts.
 */
public class AsStatementObject
{
    /**
     * The column or expression key.
     */
    private final String key;

    /**
     * Constructs a new AsStatementObject with the given key.
     *
     * @param key the column or expression key
     */
    private AsStatementObject(String key)
    {
        this.key = key;
    }

    /**
     * Creates a new AsStatementObject with the given key.
     *
     * @param key the column or expression key
     * @return a new AsStatementObject instance
     */
    public static AsStatementObject of(String key)
    {
        return new AsStatementObject(key);
    }

    /**
     * Gets the key for this object.
     *
     * @return the column or expression key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Creates a final <b>{@code AS}</b> object with the specified alias name. If the alias contains spaces, it will be wrapped in brackets.
     *
     * @param as the alias name
     * @return a FinalAsStatementObject with the alias applied
     */
    public FinalAsStatementObject as(String as)
    {
        if(as.contains(" "))
            as = "[" + as + "]";

        return new FinalAsStatementObject(key, as);
    }

    /**
     * A record representing a column or expression with its final alias.
     *
     * @param key the column or expression key
     * @param as  the alias name
     */
    public record FinalAsStatementObject(String key, String as)
    {
    }
}
