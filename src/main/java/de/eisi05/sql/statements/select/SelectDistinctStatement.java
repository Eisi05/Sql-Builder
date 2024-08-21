package de.eisi05.sql.statements.select;

import de.eisi05.sql.statements.AbstractStatement;

public class SelectDistinctStatement extends SelectStatement
{
    private SelectDistinctStatement(String... keys)
    {
        super(keys);
    }

    @Override
    protected String getKey()
    {
        return "SELECT DISTINCT";
    }

    public interface SelectDistinctStatementContainer extends AbstractStatement.StatementContainer
    {
        default SelectDistinctStatement selectDistinct(String... keys)
        {
            return create(new SelectDistinctStatement(keys));
        }
    }
}
