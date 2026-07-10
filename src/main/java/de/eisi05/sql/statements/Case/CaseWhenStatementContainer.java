package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

public interface CaseWhenStatementContainer extends AbstractStatement.StatementContainer
{
    default CaseWhenStatement when(Object value)
    {
        return create(new CaseWhenStatement("?"), value);
    }
}
