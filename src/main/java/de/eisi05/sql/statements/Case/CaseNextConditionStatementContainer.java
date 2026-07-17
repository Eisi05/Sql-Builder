package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Interface for adding additional conditions to a <b>{@code CASE WHEN}</b> expression. Supports <b>{@code AND}</b> and <b>{@code OR}</b> logic operators to
 * chain multiple conditions.
 */
public interface CaseNextConditionStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Adds an <b>{@code OR}</b> condition to the <b>{@code CASE}</b> expression.
     *
     * @param key the value for the <b>{@code OR}</b> condition
     * @return a new CaseWhenStatement with <b>{@code OR}</b> operator
     */
    default CaseWhenStatement or(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.OR), key);
    }

    /**
     * Adds an <b>{@code AND}</b> condition to the <b>{@code CASE}</b> expression.
     *
     * @param key the value for the <b>{@code AND}</b> condition
     * @return a new CaseWhenStatement with <b>{@code AND}</b> operator
     */
    default CaseWhenStatement and(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.AND), key);
    }
}
