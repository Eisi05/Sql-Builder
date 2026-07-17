package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code LEFT JOIN}</b> statement. Returns all rows from the left table and matching rows from the right table. Non-matching rows from the
 * right table are filled with <b>{@code NULL}</b> values.
 */
public class LeftJoinStatement extends JoinStatement
{
    /**
     * Constructs a new LeftJoinStatement with the given query.
     *
     * @param query the <b>{@code LEFT JOIN}</b> query fragment
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
     * Interface for containers that can create <b>{@code LEFT JOIN}</b> statements.
     */
    public interface LeftJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code LEFT JOIN}</b> statement with the specified table and join keys.
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
