package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.*;

/**
 * Abstract base class representing a WHERE clause statement in an SQL query building process.
 * <p>
 * This class extends {@link FinalStatement} and implements various containers that allow the statement to be chained with subsequent SQL components such as
 * LIMIT, ORDER BY, FETCH FIRST, FOR UPDATE, and RETURNING.
 * </p>
 */
public abstract class AbstractWhereStatement extends FinalStatement implements WhereNextStatementContainer,
                                                                               LimitStatement.LimitStatementContainer,
                                                                               OrderByStatement.OrderByStatementContainer,
                                                                               FetchFirstStatement.FetchFirstStatementContainer,
                                                                               ForUpdateStatement.ForUpdateStatementContainer,
                                                                               ReturningStatement.ReturningStatementContainer
{
    /**
     * Constructs a new {@code AbstractWhereStatement} with the given query fragment.
     *
     * @param query the partial SQL query representing this WHERE statement
     */
    protected AbstractWhereStatement(String query)
    {
        super(query);
    }
}