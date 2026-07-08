package de.eisi05.sql.statements;

public class ForUpdateStatement extends FinalStatement
{
    protected ForUpdateStatement()
    {
        super("FOR UPDATE");
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface ForUpdateStatementContainer extends StatementContainer
    {
        default ForUpdateStatement forUpdate()
        {
            return create(new ForUpdateStatement());
        }
    }
}
