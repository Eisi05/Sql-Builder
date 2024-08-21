package de.eisi05.sql.statements;

public class IntoStatement extends AbstractStatement implements InStatement.InStatementContainer,
        FromStatement.FromStatementContainer
{
    protected IntoStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "INTO";
    }

    public interface IntoStatementContainer extends StatementContainer
    {
        default IntoStatement into(String table)
        {
            return create(new IntoStatement(table));
        }
    }
}
