package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectAvgStatement extends SelectStatement
{
    private SelectAvgStatement(String key)
    {
        super("AVG(" + key + ")");
    }

    public interface SelectAvgStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectAvgStatement selectAvg(String key)
        {
            return create(new SelectAvgStatement(key));
        }
    }
}
