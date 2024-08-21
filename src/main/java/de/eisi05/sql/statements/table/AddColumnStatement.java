package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class AddColumnStatement extends FinalStatement
{
    protected AddColumnStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "ADD";
    }

    public interface AddColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        default AddColumnStatement add(TableColumn column)
        {
            return create(new AddColumnStatement(column.asQuery()));
        }
    }
}
