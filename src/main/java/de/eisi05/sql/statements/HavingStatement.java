package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereDefaultStatementContainer;

/**
 * Represents a SQL HAVING clause. Filters groups created by GROUP BY based on specified conditions. Supports aggregate functions like COUNT in the filter
 * condition.
 */
public class HavingStatement extends FinalStatement implements WhereDefaultStatementContainer,
                                                               ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new HavingStatement with the given condition.
     *
     * @param key the HAVING condition
     */
    protected HavingStatement(String key)
    {
        super(key);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "HAVING"
     */
    @Override
    protected String getKey()
    {
        return "HAVING";
    }

    /**
     * Interface for containers that can create HAVING statements.
     */
    public interface HavingStatementContainer extends StatementContainer
    {
        /**
         * Creates a HAVING statement with a COUNT condition.
         *
         * @param key the column to count
         * @return a new HavingStatement with COUNT condition
         */
        default HavingStatement hasCount(String key)
        {
            return create(new HavingStatement("COUNT(" + key + ")"));
        }
    }
}
