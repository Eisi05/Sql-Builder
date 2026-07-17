package de.eisi05.sql.statements;

/**
 * Represents a SQL <b>{@code IN}</b> clause. Used to specify multiple possible values for a column in <b>{@code WHERE}</b> conditions. Can be used with
 * subqueries or value lists.
 */
public class InStatement extends AbstractStatement implements FromStatement.FromStatementContainer
{
    /**
     * Constructs a new InStatement with the given database or value list.
     *
     * @param db the database name, table name, or value list
     */
    protected InStatement(String db)
    {
        super(db);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "IN"
     */
    @Override
    protected String getKey()
    {
        return "IN";
    }

    /**
     * Interface for containers that can create <b>{@code IN}</b> statements.
     */
    public interface InStatementContainer extends StatementContainer
    {
        /**
         * Creates an <b>{@code IN}</b> statement for the specified database or value list.
         *
         * @param db the database name, table name, or value list
         * @return a new InStatement
         */
        default InStatement in(String db)
        {
            return create(new InStatement(db));
        }
    }
}
