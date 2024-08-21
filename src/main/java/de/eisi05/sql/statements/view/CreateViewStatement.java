package de.eisi05.sql.statements.view;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatementContainer;

public class CreateViewStatement extends AbstractStatement implements AbstractStatement.StatementContainer,
        ExecuteUpdateStatement
{
    protected CreateViewStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "CREATE VIEW";
    }

    public CreateViewAsStatement as()
    {
        return create(new CreateViewAsStatement("AS"));
    }

    public interface CreateViewStatementContainer extends StatementContainer
    {
        default CreateViewStatement createView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new CreateViewStatement(name));
        }
    }

    public static class CreateViewAsStatement extends AbstractStatement implements SelectStatementContainer
    {
        protected CreateViewAsStatement(String query)
        {
            super(query);
        }

        @Override
        protected String getKey()
        {
            return "";
        }
    }
}
