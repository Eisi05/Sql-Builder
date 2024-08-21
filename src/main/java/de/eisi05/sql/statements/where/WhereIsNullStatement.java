package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;

public class WhereIsNullStatement extends AbstractWhereStatement
{
    protected WhereIsNullStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "IS";
    }

    public interface WhereIsNullStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereIsNullStatement isNull()
        {
            return create(new WhereIsNullStatement("NULL"));
        }
    }
}
