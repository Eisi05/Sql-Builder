package de.eisi05.sql.statements.where;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractStatement;

public interface WhereNextStatementContainer extends AbstractStatement.StatementContainer
{
    default WhereStatement or(String key)
    {
        return create(new WhereStatement(key).withOperation(LogicOperator.OR));
    }

    default WhereStatement and(String key)
    {
        return create(new WhereStatement(key).withOperation(LogicOperator.AND));
    }
}
