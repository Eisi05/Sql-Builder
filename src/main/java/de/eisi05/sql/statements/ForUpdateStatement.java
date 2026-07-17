package de.eisi05.sql.statements;

/**
 * Represents a SQL <b>{@code FOR UPDATE}</b> statement for row-level locking. Used to lock selected rows for update in a transaction.
 */
public class ForUpdateStatement extends FinalStatement
{
    /**
     * Constructs a new ForUpdateStatement.
     */
    protected ForUpdateStatement()
    {
        super("FOR UPDATE");
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since <b>{@code FOR UPDATE}</b> doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create <b>{@code FOR UPDATE}</b> statements.
     */
    public interface ForUpdateStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code FOR UPDATE}</b> statement to lock rows.
         *
         * @return a new ForUpdateStatement
         */
        default ForUpdateStatement forUpdate()
        {
            return create(new ForUpdateStatement());
        }
    }
}
