package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectMinStatement extends SelectStatement
{
    private SelectMinStatement(String key)
    {
        super("MIN(" + key + ")");
    }

    public interface SelectMinStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectMinStatement selectMin(String key)
        {
            return create(new SelectMinStatement(key));
        }
    }
}
