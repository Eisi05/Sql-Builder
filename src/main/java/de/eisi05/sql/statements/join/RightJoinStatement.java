package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

public class RightJoinStatement extends JoinStatement
{
    protected RightJoinStatement(String query)
    {
        super(query);
    }

    @Override
    JoinType getJoinType()
    {
        return JoinType.RIGHT;
    }

    public interface RightJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        default RightJoinStatement rightJoin(String table, String table1Key, String table2Key)
        {
            return create(new RightJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
