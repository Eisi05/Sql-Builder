package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.Collections;

public class WhereNotInStatement extends WhereInStatement implements WhereNotStatement
{
    protected WhereNotInStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    public interface WhereNotInStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotInStatement notIn(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new WhereNotInStatement("(" + placeholders + ")"), o);
        }

        default WhereNotInStatement notIn(FinalStatement finalStatement)
        {
            return create(new WhereNotInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
