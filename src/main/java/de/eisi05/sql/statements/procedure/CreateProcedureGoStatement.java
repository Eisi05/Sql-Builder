package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class CreateProcedureGoStatement extends FinalStatement
{
    protected CreateProcedureGoStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface CreateProcedureGoStatementContainer extends AbstractStatement.StatementContainer
    {
        default CreateProcedureGoStatement go()
        {
            return create(new CreateProcedureGoStatement("GO"));
        }
    }
}
