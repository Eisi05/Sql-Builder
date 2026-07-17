package de.eisi05.sql.statements;

/**
 * A custom SQL statement that allows adding arbitrary SQL fragments to a query chain. Useful for database-specific syntax or custom SQL operations not covered
 * by the standard API.
 */
public class CustomStatement extends FinalStatement
{
    /**
     * Constructs a new CustomStatement with the given query.
     *
     * @param query the custom SQL fragment
     */
    CustomStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since custom statements don't have a keyword.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }
}
