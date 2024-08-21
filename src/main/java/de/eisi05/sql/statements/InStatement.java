package de.eisi05.sql.statements;

public class InStatement extends AbstractStatement implements FromStatement.FromStatementContainer
{
    protected InStatement(String db)
    {
        super(db);
    }

    @Override
    protected String getKey()
    {
        return "IN";
    }

    public interface InStatementContainer extends StatementContainer
    {
        default InStatement in(String db)
        {
            return create(new InStatement(db));
        }
    }
}
