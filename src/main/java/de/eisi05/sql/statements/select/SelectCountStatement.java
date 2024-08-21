package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectCountStatement extends SelectStatement
{
    private SelectCountStatement(String key)
    {
        super("COUNT(" + key + ")");
    }

    public interface SelectCountStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectCountStatement selectCount(String key)
        {
            return create(new SelectCountStatement(key));
        }

        default SelectCountStatement selectCountDistinct(String key)
        {
            return create(new SelectCountStatement("DISTINCT " + key));
        }
    }
}
