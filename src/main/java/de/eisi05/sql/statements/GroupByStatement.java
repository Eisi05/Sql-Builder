package de.eisi05.sql.statements;

public class GroupByStatement extends FinalStatement implements HavingStatement.HavingStatementContainer
{
    protected GroupByStatement(String key)
    {
        super(key);
    }

    @Override
    protected String getKey()
    {
        return "GROUP BY";
    }

    public interface GroupByStatementContainer extends StatementContainer
    {
        default GroupByStatement groupBy(String key)
        {
            return create(new GroupByStatement(key));
        }
    }
}
