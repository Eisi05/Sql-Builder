package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectSumStatement extends SelectStatement
{
    private SelectSumStatement(String key)
    {
        super("SUM(" + key + ")");
    }

    public interface SelectSumStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectSumStatement selectSum(String key)
        {
            return create(new SelectSumStatement(key));
        }
    }
}
