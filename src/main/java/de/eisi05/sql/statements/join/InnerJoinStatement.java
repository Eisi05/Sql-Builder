package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL <b>{@code INNER JOIN}</b> statement. Returns only rows where there is a match in both tables.
 */
public class InnerJoinStatement extends JoinStatement
{
    /**
     * Constructs a new InnerJoinStatement with the given query.
     *
     * @param query the <b>{@code INNER JOIN}</b> query fragment
     */
    protected InnerJoinStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the join type for this statement.
     *
     * @return JoinType.INNER
     */
    @Override
    JoinType getJoinType()
    {
        return JoinType.INNER;
    }

    /**
     * Interface for containers that can create <b>{@code INNER JOIN}</b> statements.
     */
    public interface InnerJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates an <b>{@code INNER JOIN}</b> statement with the specified table and join keys.
         *
         * @param table     the table to join
         * @param table1Key the key from the first table
         * @param table2Key the key from the second table
         * @return a new InnerJoinStatement
         */
        default InnerJoinStatement innerJoin(String table, String table1Key, String table2Key)
        {
            return create(new InnerJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
