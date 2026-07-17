package de.eisi05.sql.statements;

/**
 * Represents a SQL <b>{@code GROUP BY}</b> clause. Groups rows that have the same values into summary rows. Can be followed by <b>{@code HAVING}</b> for
 * filtering groups.
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
     * Interface for containers that can create <b>{@code GROUP BY}</b> statements.
     */
    public interface GroupByStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code GROUP BY}</b> statement for the specified column.
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
