package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.*;

public abstract class AbstractWhereStatement extends FinalStatement implements WhereNextStatementContainer,
                                                                               LimitStatement.LimitStatementContainer,
                                                                               OrderByStatement.OrderByStatementContainer,
                                                                               FetchFirstStatement.FetchFirstStatementContainer,
                                                                               ForUpdateStatement.ForUpdateStatementContainer,
                                                                               ReturningStatement.ReturningStatementContainer
{
    protected AbstractWhereStatement(String query)
    {
        super(query);
    }
}
