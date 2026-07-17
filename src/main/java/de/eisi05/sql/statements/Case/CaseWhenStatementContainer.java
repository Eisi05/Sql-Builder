package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Interface for containers that can create <b>{@code WHEN}</b> statements in a <b>{@code CASE}</b> expression.
 */
public interface CaseWhenStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Creates a <b>{@code WHEN}</b> statement with the specified value.
     *
     * @param value the value to compare
     * @return a new CaseWhenStatement
     */
    default CaseWhenStatement when(Object value)
    {
        return create(new CaseWhenStatement("?"), value);
    }
}
