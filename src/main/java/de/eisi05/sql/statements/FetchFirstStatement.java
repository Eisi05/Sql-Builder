package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

@SqlData(value = {DatabaseType.ORACLE}, oracleVersion = 12)
public class FetchFirstStatement extends FinalStatement implements AbstractStatement.StatementContainer
{
    protected FetchFirstStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "FETCH FIRST";
    }

    public interface FetchFirstStatementContainer extends AbstractStatement.StatementContainer
    {
        default FetchFirstStatement fetchFirst(int amount)
        {
            return create(new FetchFirstStatement(amount + " ROWS ONLY"));
        }

        default FetchFirstStatement fetchFirstPercent(int percent)
        {
            return create(new FetchFirstStatement(percent + " PERCENT ROWS ONLY"));
        }
    }
}
