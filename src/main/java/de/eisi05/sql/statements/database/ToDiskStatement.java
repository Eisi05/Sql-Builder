package de.eisi05.sql.statements.database;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class ToDiskStatement extends FinalStatement implements AbstractStatement.StatementContainer
{
    protected ToDiskStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "TO DISK =";
    }

    public FinalStatement withDifferential()
    {
        return create(new FinalStatement("DIFFERENTIAL")
        {
            @Override
            protected String getKey()
            {
                return "WITH";
            }
        });
    }

    public interface ToDiskStatementContainer extends StatementContainer
    {
        default ToDiskStatement toDisk(String filePath)
        {
            return create(new ToDiskStatement("'" + filePath + "'"));
        }
    }
}
