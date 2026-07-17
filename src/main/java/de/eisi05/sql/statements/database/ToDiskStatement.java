package de.eisi05.sql.statements.database;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a <b>{@code TO DISK}</b> clause for database backup statements. Specifies the file path where the backup should be stored. SQL Server-specific
 * syntax.
 */
public class ToDiskStatement extends FinalStatement implements AbstractStatement.StatementContainer
{
    /**
     * Constructs a new ToDiskStatement with the given file path.
     *
     * @param query the file path
     */
    protected ToDiskStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "TO DISK ="
     */
    @Override
    protected String getKey()
    {
        return "TO DISK =";
    }

    /**
     * Creates a <b>{@code WITH DIFFERENTIAL}</b> clause for differential backups.
     *
     * @return a FinalStatement with the <b>{@code DIFFERENTIAL}</b> option
     */
    public FinalStatement withDifferential()
    {
        return create(new FinalStatement("DIFFERENTIAL")
        {
            @Override
            protected String getKey()
            {
                return "WITH";
            }
        });
    }

    /**
     * Interface for containers that can create <b>{@code TO DISK}</b> statements.
     */
    public interface ToDiskStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code TO DISK}</b> statement with the specified file path.
         *
         * @param filePath the file path for the backup
         * @return a new ToDiskStatement
         */
        default ToDiskStatement toDisk(String filePath)
        {
            return create(new ToDiskStatement("'" + filePath + "'"));
        }
    }
}
