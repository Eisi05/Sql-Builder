package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

/**
 * Represents a SQL LIMIT clause for limiting result rows. MySQL-specific syntax. Limits the number of rows returned by a query.
 */
@SqlData(DatabaseType.MYSQL)
public class LimitStatement extends FinalStatement implements ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new LimitStatement with the given row limit.
     *
     * @param amount the maximum number of rows to return
     */
    private LimitStatement(long amount)
    {
        super(String.valueOf(amount));
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "LIMIT"
     */
    @Override
    protected String getKey()
    {
        return "LIMIT";
    }

    /**
     * Interface for containers that can create LIMIT statements.
     */
    public interface LimitStatementContainer extends StatementContainer
    {
        /**
         * Creates a LIMIT statement with the specified row count.
         *
         * @param amount the maximum number of rows to return
         * @return a new LimitStatement
         */
        default LimitStatement limit(long amount)
        {
            return create(new LimitStatement(amount));
        }
    }
}
