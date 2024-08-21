package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;

public class WhereNotIsNullStatement extends WhereIsNullStatement
{
    protected WhereNotIsNullStatement(String query)
    {
        super(query);
    }

    public interface WhereNotIsNullStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotIsNullStatement isNotNull()
        {
            return create(new WhereNotIsNullStatement("NOT NULL"));
        }
    }
}
