package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

public class ExecuteProcedureStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected ExecuteProcedureStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "EXEC";
    }

    public interface ExecuteProcedureStatementContainer extends StatementContainer
    {
        default ExecuteProcedureStatement execute(String name)
        {
            return create(new ExecuteProcedureStatement(name));
        }
    }
}
