package de.eisi05.sql.statements;

public class CustomStatement extends FinalStatement
{
    CustomStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }
}
