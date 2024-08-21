package de.eisi05.sql.statements.view;

import de.eisi05.sql.statements.AbstractStatement;

public class CreateOrReplaceViewStatement extends CreateViewStatement
{
    protected CreateOrReplaceViewStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "CREATE OR REPLACE VIEW";
    }

    public interface CreateOrReplaceViewStatementContainer extends AbstractStatement.StatementContainer
    {
        default CreateOrReplaceViewStatement createOrReplaceView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new CreateOrReplaceViewStatement(name));
        }
    }
}
