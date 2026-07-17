package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

/**
 * Represents a SQL <b>{@code FETCH FIRST}</b> statement for limiting result rows. Oracle-specific syntax (requires Oracle 12+). Supports both row count and
 * percentage-based limiting.
 */
@SqlData(value = {DatabaseType.ORACLE}, oracleVersion = 12)
public class FetchFirstStatement extends FinalStatement implements ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new FetchFirstStatement with the given query.
     *
     * @param query the <b>{@code FETCH FIRST}</b> query fragment
     */
    protected FetchFirstStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "FETCH FIRST"
     */
    @Override
    protected String getKey()
    {
        return "FETCH FIRST";
    }

    /**
     * Interface for containers that can create <b>{@code FETCH FIRST}</b> statements.
     */
    public interface FetchFirstStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code FETCH FIRST}</b> statement with a row count limit.
         *
         * @param amount the number of rows to fetch
         * @return a new FetchFirstStatement
         */
        default FetchFirstStatement fetchFirst(int amount)
        {
            return create(new FetchFirstStatement(amount + " ROWS ONLY"));
        }

        /**
         * Creates a <b>{@code FETCH FIRST}</b> statement with a percentage limit.
         *
         * @param percent the percentage of rows to fetch
         * @return a new FetchFirstStatement
         */
        default FetchFirstStatement fetchFirstPercent(int percent)
        {
            return create(new FetchFirstStatement(percent + " PERCENT ROWS ONLY"));
        }
    }
}
