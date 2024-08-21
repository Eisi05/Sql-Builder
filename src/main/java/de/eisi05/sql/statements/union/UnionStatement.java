package de.eisi05.sql.statements.union;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatement;

public class UnionStatement extends AbstractStatement implements SelectStatement.SelectStatementContainer
{
    protected UnionStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }
}
