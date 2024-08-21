package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;

public class InnerJoinStatement extends JoinStatement
{
    protected InnerJoinStatement(String query)
    {
        super(query);
    }

    @Override
    JoinType getJoinType()
    {
        return JoinType.INNER;
    }

    public interface InnerJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        default InnerJoinStatement innerJoin(String table, String table1Key, String table2Key)
        {
            return create(new InnerJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
