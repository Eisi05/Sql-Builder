package de.eisi05.sql.statements;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.where.WhereStatement;

public class DeleteStatement extends FinalStatement implements WhereStatement.WhereStatementContainer,
        ExecuteUpdateStatement
{
    private DeleteStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "DELETE FROM";
    }

    public interface DeleteStatementContainer extends StatementContainer
    {
        default DeleteStatement delete(String table)
        {
            return create(new DeleteStatement(table));
        }
    }
}
