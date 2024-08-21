package de.eisi05.sql.statements.view;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class DropViewStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected DropViewStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "DROP VIEW";
    }

    public interface DropViewStatementContainer extends AbstractStatement.StatementContainer
    {
        default DropViewStatement dropView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new DropViewStatement(name));
        }
    }
}
