package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

public class CreateIndexStatement extends AbstractStatement implements OnIndexStatement.OnIndexStatementContainer,
        ExecuteUpdateStatement
{
    private final boolean unique;

    protected CreateIndexStatement(String index, boolean unique)
    {
        super(index);
        this.unique = unique;
    }

    @Override
    protected String getKey()
    {
        return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX";
    }

    public interface CreateIndexStatementContainer extends StatementContainer
    {
        default CreateIndexStatement createIndex(String index)
        {
            return create(new CreateIndexStatement(index, false));
        }

        default CreateIndexStatement createUniqueIndex(String index)
        {
            return create(new CreateIndexStatement(index, true));
        }
    }
}
