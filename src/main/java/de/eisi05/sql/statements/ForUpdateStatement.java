package de.eisi05.sql.statements;

/**
 * Represents a SQL FOR UPDATE statement for row-level locking. Used to lock selected rows for update in a transaction.
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
     * Gets the SQL keyword for this statement. Returns empty string since FOR UPDATE doesn't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create FOR UPDATE statements.
     */
    public interface ForUpdateStatementContainer extends StatementContainer
    {
        /**
         * Creates a FOR UPDATE statement to lock rows.
         *
         * @return a new ForUpdateStatement
         */
        default ForUpdateStatement forUpdate()
        {
            return create(new ForUpdateStatement());
        }
    }
}
