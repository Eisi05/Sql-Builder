package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectMaxStatement extends SelectStatement
{
    public SelectMaxStatement(String key)
    {
        super("MAX(" + key + ")");
    }

    public interface SelectMaxStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectMaxStatement selectMax(String key)
        {
            return create(new SelectMaxStatement(key));
        }
    }
}
