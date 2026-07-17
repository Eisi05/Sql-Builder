package de.eisi05.sql.statements.procedure;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents the <b>{@code GO}</b> statement in SQL Server. Used to signal the end of a batch of SQL statements. SQL Server-specific syntax.
 */
public class CreateProcedureGoStatement extends FinalStatement
{
    /**
     * Constructs a new CreateProcedureGoStatement.
     *
     * @param query the <b>{@code GO}</b> keyword
     */
    protected CreateProcedureGoStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since <b>{@code GO}</b> doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create <b>{@code GO}</b> statements.
     */
    public interface CreateProcedureGoStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code GO}</b> statement to end the batch.
         *
         * @return a new CreateProcedureGoStatement
         */
        default CreateProcedureGoStatement go()
        {
            return create(new CreateProcedureGoStatement("GO"));
        }
    }
}
