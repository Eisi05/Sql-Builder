package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

public class CreateProcedureStatement extends AbstractStatement
        implements CreateProcedureAsStatement.CreateProcedureAsStatementContainer, ExecuteUpdateStatement
{
    protected CreateProcedureStatement(String name)
    {
        super(name);
    }

    @Override
    protected String getKey()
    {
        return "CREATE PROCEDURE";
    }

    public interface CreateProcedureStatementContainer extends StatementContainer
    {
        default CreateProcedureStatement createProcedure(String name)
        {
            return create(new CreateProcedureStatement(name));
        }
    }
}
