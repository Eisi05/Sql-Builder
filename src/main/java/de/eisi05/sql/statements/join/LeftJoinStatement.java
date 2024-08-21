package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

public class LeftJoinStatement extends JoinStatement
{
    protected LeftJoinStatement(String query)
    {
        super(query);
    }

    @Override
    JoinType getJoinType()
    {
        return JoinType.LEFT;
    }

    public interface LeftJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        default LeftJoinStatement leftJoin(String table, String table1Key, String table2Key)
        {
            return create(new LeftJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
