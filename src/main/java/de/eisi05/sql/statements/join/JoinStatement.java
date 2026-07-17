package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.statements.OrderByStatement;

/**
 * Abstract base class for SQL JOIN statements. Supports different join types (INNER, LEFT, RIGHT, FULL OUTER). Can be followed by ORDER BY clauses.
 */
public abstract class JoinStatement extends FinalStatement implements OrderByStatement.OrderByStatementContainer
{
    /**
     * Constructs a new JoinStatement with the given query.
     *
     * @param query the JOIN query fragment
     */
    protected JoinStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the join type for this statement.
     *
     * @return the JoinType
     */
    abstract JoinType getJoinType();

    /**
     * Gets the SQL keyword for this statement. Returns the join type name with underscores removed, followed by " JOIN".
     *
     * @return the SQL keyword (e.g., "INNER JOIN", "LEFT JOIN")
     */
    @Override
    protected String getKey()
    {
        return getJoinType().name().replace("_", "") + " JOIN";
    }

    /**
     * Enumeration of supported join types.
     */
    enum JoinType
    {
        /**
         * Inner join - only matching rows
         */
        INNER,
        /**
         * Left outer join - all rows from left table
         */
        LEFT,
        /**
         * Right outer join - all rows from right table
         */
        RIGHT,
        /**
         * Full outer join - all rows from both tables
         */
        FULL_OUTER
    }
}
