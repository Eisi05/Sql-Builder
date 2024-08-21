package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectAllStatement extends SelectStatement
{
    private SelectAllStatement(String... keys)
    {
        super(String.join(", ", keys));
    }

    @Override
    protected String getKey()
    {
        return "SELECT ALL";
    }

    public interface SelectAllStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectAllStatement selectAll(String... keys)
        {
            return create(new SelectAllStatement(keys));
        }
    }
}
