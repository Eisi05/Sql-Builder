package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL EXEC statement for executing stored procedures. Executes a stored procedure with the specified name. SQL Server-specific syntax.
 */
public class ExecuteProcedureStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new ExecuteProcedureStatement for the specified procedure.
     *
     * @param query the procedure name
     */
    protected ExecuteProcedureStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "EXEC"
     */
    @Override
    protected String getKey()
    {
        return "EXEC";
    }

    /**
     * Interface for containers that can create EXEC statements.
     */
    public interface ExecuteProcedureStatementContainer extends StatementContainer
    {
        /**
         * Creates an EXEC statement for the specified procedure.
         *
         * @param name the procedure name
         * @return a new ExecuteProcedureStatement
         */
        default ExecuteProcedureStatement execute(String name)
        {
            return create(new ExecuteProcedureStatement(name));
        }
    }
}
