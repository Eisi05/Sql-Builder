package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL CREATE PROCEDURE statement. Creates a new stored procedure with the specified name. SQL Server-specific syntax.
 */
public class CreateProcedureStatement extends AbstractStatement
        implements CreateProcedureAsStatement.CreateProcedureAsStatementContainer, ExecuteUpdateStatement
{
    /**
     * Constructs a new CreateProcedureStatement for the specified procedure.
     *
     * @param name the procedure name
     */
    protected CreateProcedureStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "CREATE PROCEDURE"
     */
    @Override
    protected String getKey()
    {
        return "CREATE PROCEDURE";
    }

    /**
     * Interface for containers that can create CREATE PROCEDURE statements.
     */
    public interface CreateProcedureStatementContainer extends StatementContainer
    {
        /**
         * Creates a CREATE PROCEDURE statement for the specified procedure.
         *
         * @param name the procedure name
         * @return a new CreateProcedureStatement
         */
        default CreateProcedureStatement createProcedure(String name)
        {
            return create(new CreateProcedureStatement(name));
        }
    }
}
