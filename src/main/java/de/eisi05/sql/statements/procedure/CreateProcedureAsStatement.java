package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class CreateProcedureAsStatement extends AbstractStatement
        implements CreateProcedureGoStatement.CreateProcedureGoStatementContainer
{
    protected CreateProcedureAsStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "AS";
    }

    public interface CreateProcedureAsStatementContainer extends AbstractStatement.StatementContainer
    {
        default CreateProcedureAsStatement as(FinalStatement finalStatement)
        {
            return create(new CreateProcedureAsStatement(finalStatement.getQuery()));
        }
    }
}
