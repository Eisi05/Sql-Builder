package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

public interface CaseNextConditionStatementContainer extends AbstractStatement.StatementContainer
{
    default CaseWhenStatement or(String key)
    {
        return create(new CaseWhenStatement(key).withOperation(LogicOperator.OR));
    }

    default CaseWhenStatement and(String key)
    {
        return create(new CaseWhenStatement(key).withOperation(LogicOperator.AND));
    }
}
