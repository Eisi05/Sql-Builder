package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

public interface CaseNextConditionStatementContainer extends AbstractStatement.StatementContainer
{
    default CaseWhenStatement or(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.OR), key);
    }

    default CaseWhenStatement and(Object key)
    {
        return create(new CaseWhenStatement("?").withOperation(LogicOperator.AND), key);
    }
}
