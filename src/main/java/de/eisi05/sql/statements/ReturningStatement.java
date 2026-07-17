package de.eisi05.sql.statements;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;

import java.util.Collection;

/**
 * Represents a SQL <b>{@code RETURNING}</b> clause. Returns data from modified rows after <b>{@code INSERT}</b> , <b>{@code UPDATE}</b> , or
 * <b>{@code DELETE}</b> operations. PostgreSQL-specific syntax.
 */
public class ReturningStatement extends FinalStatement implements ExecuteQueryStatement
{
    /**
     * Constructs a new ReturningStatement for the specified columns.
     *
     * @param key the column(s) to return
     */
    protected ReturningStatement(String key)
    {
        super(key);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "RETURNING"
     */
    @Override
    protected String getKey()
    {
        return "RETURNING";
    }

    /**
     * Interface for containers that can create <b>{@code RETURNING}</b> statements.
     */
    public interface ReturningStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code RETURNING}</b> statement for a single column.
         *
         * @param key the column to return
         * @return a new ReturningStatement
         */
        default ReturningStatement returning(String key)
        {
            return create(new ReturningStatement(key));
        }

        /**
         * Creates a <b>{@code RETURNING}</b> statement for multiple columns.
         *
         * @param keys the collection of columns to return
         * @return a new ReturningStatement
         */
        default ReturningStatement returning(Collection<String> keys)
        {
            return create(new ReturningStatement(String.join(", ", keys)));
        }

        /**
         * Creates a <b>{@code RETURNING}</b> statement for multiple columns.
         *
         * @param keys the array of columns to return
         * @return a new ReturningStatement
         */
        default ReturningStatement returning(String[] keys)
        {
            return create(new ReturningStatement(String.join(", ", keys)));
        }
    }
}
