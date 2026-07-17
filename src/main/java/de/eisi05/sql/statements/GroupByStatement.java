package de.eisi05.sql.statements;

/**
 * Represents a SQL GROUP BY clause. Groups rows that have the same values into summary rows. Can be followed by HAVING for filtering groups.
 */
public class GroupByStatement extends FinalStatement implements HavingStatement.HavingStatementContainer,
                                                                ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new GroupByStatement for the specified column.
     *
     * @param key the column to group by
     */
    protected GroupByStatement(String key)
    {
        super(key);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "GROUP BY"
     */
    @Override
    protected String getKey()
    {
        return "GROUP BY";
    }

    /**
     * Interface for containers that can create GROUP BY statements.
     */
    public interface GroupByStatementContainer extends StatementContainer
    {
        /**
         * Creates a GROUP BY statement for the specified column.
         *
         * @param key the column to group by
         * @return a new GroupByStatement
         */
        default GroupByStatement groupBy(String key)
        {
            return create(new GroupByStatement(key));
        }
    }
}
