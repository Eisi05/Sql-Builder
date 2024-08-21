package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public interface CaseThenStatementContainer extends AbstractStatement.StatementContainer
{
    default CaseThenStatement thenKey(String key)
    {
        return create(new CaseThenStatement(key));
    }

    default CaseThenStatement thenValue(Object value)
    {
        if(value instanceof String)
            return create(new CaseThenStatement("'" + value + "'"));
        else
            return create(new CaseThenStatement(value.toString()));
    }
}
