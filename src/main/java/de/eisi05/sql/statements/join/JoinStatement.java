package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.statements.OrderByStatement;

public abstract class JoinStatement extends FinalStatement implements OrderByStatement.OrderByStatementContainer
{
    protected JoinStatement(String query)
    {
        super(query);
    }

    abstract JoinType getJoinType();

    @Override
    protected String getKey()
    {
        return getJoinType().name().replace("_", "") + " JOIN";
    }

    enum JoinType
    {
        INNER,
        LEFT,
        RIGHT,
        FULL_OUTER
    }
}
