package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents the AS clause in a CREATE PROCEDURE statement. Specifies the body of the stored procedure. SQL Server-specific syntax.
 */
public class CreateProcedureAsStatement extends AbstractStatement
        implements CreateProcedureGoStatement.CreateProcedureGoStatementContainer
{
    /**
     * Constructs a new CreateProcedureAsStatement with the given procedure body.
     *
     * @param query the procedure body SQL
     */
    protected CreateProcedureAsStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "AS"
     */
    @Override
    protected String getKey()
    {
        return "AS";
    }

    /**
     * Interface for containers that can create AS statements in CREATE PROCEDURE.
     */
    public interface CreateProcedureAsStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates an AS statement with the specified procedure body.
         *
         * @param finalStatement the procedure body SQL
         * @return a new CreateProcedureAsStatement
         */
        default CreateProcedureAsStatement as(FinalStatement finalStatement)
        {
            return create(new CreateProcedureAsStatement(finalStatement.getQuery()));
        }
    }
}
