package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereExistsStatement extends FinalStatement
{
    protected WhereExistsStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface WhereExistsStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereExistsStatement whereExists(FinalStatement finalStatement)
        {
            return create(new WhereExistsStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
