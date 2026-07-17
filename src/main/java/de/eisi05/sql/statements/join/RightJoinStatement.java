package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code RIGHT JOIN}</b> statement. Returns all rows from the right table and matching rows from the left table. Non-matching rows from the
 * left table are filled with <b>{@code NULL}</b> values.
 */
public class RightJoinStatement extends JoinStatement
{
    /**
     * Constructs a new RightJoinStatement with the given query.
     *
     * @param query the <b>{@code RIGHT JOIN}</b> query fragment
     */
    protected RightJoinStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the join type for this statement.
     *
     * @return JoinType.RIGHT
     */
    @Override
    JoinType getJoinType()
    {
        return JoinType.RIGHT;
    }

    /**
     * Interface for containers that can create <b>{@code RIGHT JOIN}</b> statements.
     */
    public interface RightJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a <b>{@code RIGHT JOIN}</b> statement with the specified table and join keys.
         *
         * @param table     the table to join
         * @param table1Key the key from the first table
         * @param table2Key the key from the second table
         * @return a new RightJoinStatement
         */
        default RightJoinStatement rightJoin(String table, String table1Key, String table2Key)
        {
            return create(new RightJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
