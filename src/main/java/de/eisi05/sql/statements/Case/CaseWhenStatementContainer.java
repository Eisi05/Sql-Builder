package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Interface for containers that can create WHEN statements in a CASE expression.
 */
public interface CaseWhenStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Creates a WHEN statement with the specified value.
     *
     * @param value the value to compare
     * @return a new CaseWhenStatement
     */
    default CaseWhenStatement when(Object value)
    {
        return create(new CaseWhenStatement("?"), value);
    }
}
