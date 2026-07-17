package de.eisi05.sql.statements.where;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Provides capability for chaining multiple WHERE operations sequentially using logical conjunctions or disjunctions.
 */
public interface WhereNextStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Chains a subsequent query condition using an {@code OR} logical operator.
     *
     * @param key the database column or key expression context for the next condition
     * @return a updated {@link WhereStatement} initialized with an OR relation
     */
    default WhereStatement or(String key)
    {
        return create(new WhereStatement(key).withOperation(LogicOperator.OR));
    }

    /**
     * Chains a subsequent query condition using an {@code AND} logical operator.
     *
     * @param key the database column or key expression context for the next condition
     * @return an updated {@link WhereStatement} initialized with an AND relation
     */
    default WhereStatement and(String key)
    {
        return create(new WhereStatement(key).withOperation(LogicOperator.AND));
    }
}