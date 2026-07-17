package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.*;

/**
 * Abstract base class representing a <b>{@code WHERE}</b> clause statement in an SQL query building process.
 * <p>
 * This class extends {@link FinalStatement} and implements various containers that allow the statement to be chained with subsequent SQL components such as
 * <b>{@code LIMIT}</b> , <b>{@code ORDER BY}</b> , <b>{@code FETCH FIRST}</b> , <b>{@code FOR UPDATE}</b> , and <b>{@code RETURNING}</b> .
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
     * @param query the partial SQL query representing this <b>{@code WHERE}</b> statement
     */
    protected AbstractWhereStatement(String query)
    {
        super(query);
    }
}