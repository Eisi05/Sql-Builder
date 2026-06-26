package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.utils.OrmUtils;

public interface CaseThenStatementContainer extends AbstractStatement.StatementContainer
{
    default CaseThenStatement thenKey(String key)
    {
        return create(new CaseThenStatement(key));
    }

    default CaseThenStatement thenValue(Object value)
    {
        return create(new CaseThenStatement(OrmUtils.formatValue(value)));
    }
}
