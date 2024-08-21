package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.FetchFirstStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.statements.LimitStatement;
import de.eisi05.sql.statements.OrderByStatement;

public abstract class AbstractWhereStatement extends FinalStatement implements WhereNextStatementContainer,
        LimitStatement.LimitStatementContainer, OrderByStatement.OrderByStatementContainer,
        FetchFirstStatement.FetchFirstStatementContainer
{
    protected AbstractWhereStatement(String query)
    {
        super(query);
    }
}
