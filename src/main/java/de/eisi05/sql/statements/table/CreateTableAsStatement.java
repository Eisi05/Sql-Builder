package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatement;

public class CreateTableAsStatement extends AbstractStatement implements SelectStatement.SelectStatementContainer
{
    protected CreateTableAsStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface CreateTableAsStatementContainer extends StatementContainer
    {
        default CreateTableAsStatement as()
        {
            return create(new CreateTableAsStatement("AS"));
        }
    }
}
