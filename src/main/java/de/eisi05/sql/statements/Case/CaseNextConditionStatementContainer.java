package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Interface for adding additional conditions to a CASE WHEN expression. Supports AND and OR logic operators to chain multiple conditions.
 */
public interface CaseNextConditionStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Adds an OR condition to the CASE expression.
     *
     * @param key the value for the OR condition
     * @return a new CaseWhenStatement with OR operator
     */
    default CaseWhenStatement or(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.OR), key);
    }

    /**
     * Adds an AND condition to the CASE expression.
     *
     * @param key the value for the AND condition
     * @return a new CaseWhenStatement with AND operator
     */
    default CaseWhenStatement and(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.AND), key);
    }
}
