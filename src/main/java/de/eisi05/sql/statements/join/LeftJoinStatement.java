package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL LEFT JOIN statement. Returns all rows from the left table and matching rows from the right table. Non-matching rows from the right table are
 * filled with NULL values.
 */
public class LeftJoinStatement extends JoinStatement
{
    /**
     * Constructs a new LeftJoinStatement with the given query.
     *
     * @param query the LEFT JOIN query fragment
     */
    protected LeftJoinStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the join type for this statement.
     *
     * @return JoinType.LEFT
     */
    @Override
    JoinType getJoinType()
    {
        return JoinType.LEFT;
    }

    /**
     * Interface for containers that can create LEFT JOIN statements.
     */
    public interface LeftJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a LEFT JOIN statement with the specified table and join keys.
         *
         * @param table     the table to join
         * @param table1Key the key from the first table
         * @param table2Key the key from the second table
         * @return a new LeftJoinStatement
         */
        default LeftJoinStatement leftJoin(String table, String table1Key, String table2Key)
        {
            return create(new LeftJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
