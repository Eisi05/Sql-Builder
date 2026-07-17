package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.where.WhereStatement;

/**
 * Represents a SQL FULL OUTER JOIN statement. Combines rows from both tables, matching rows when possible and including non-matching rows from both sides.
 */
public class FullJoinStatement extends JoinStatement implements WhereStatement.WhereStatementContainer
{
    /**
     * Constructs a new FullJoinStatement with the given query.
     *
     * @param query the FULL JOIN query fragment
     */
    protected FullJoinStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the join type for this statement.
     *
     * @return JoinType.FULL_OUTER
     */
    @Override
    JoinType getJoinType()
    {
        return JoinType.FULL_OUTER;
    }

    /**
     * Interface for containers that can create FULL JOIN statements.
     */
    public interface FullJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a FULL JOIN statement with the specified table and join keys.
         *
         * @param table     the table to join
         * @param table1Key the key from the first table
         * @param table2Key the key from the second table
         * @return a new FullJoinStatement
         */
        default FullJoinStatement fullJoin(String table, String table1Key, String table2Key)
        {
            return create(new FullJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
