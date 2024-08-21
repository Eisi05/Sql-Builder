package de.eisi05.sql.statements.join;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.where.WhereStatement;

public class FullJoinStatement extends JoinStatement implements WhereStatement.WhereStatementContainer
{
    protected FullJoinStatement(String query)
    {
        super(query);
    }

    @Override
    JoinType getJoinType()
    {
        return JoinType.FULL_OUTER;
    }

    public interface FullJoinStatementContainer extends AbstractStatement.StatementContainer
    {
        default FullJoinStatement fullJoin(String table, String table1Key, String table2Key)
        {
            return create(new FullJoinStatement(table + " ON " + table1Key + " = " + table2Key));
        }
    }
}
