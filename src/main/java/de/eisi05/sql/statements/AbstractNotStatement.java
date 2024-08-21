package de.eisi05.sql.statements;

public abstract class AbstractNotStatement extends AbstractStatement
{
    protected boolean withNot = false;

    protected AbstractNotStatement(String query)
    {
        super(query);
    }
}
