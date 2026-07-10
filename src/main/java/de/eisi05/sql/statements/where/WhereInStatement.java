package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class WhereInStatement extends AbstractWhereStatement
{
    protected WhereInStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "IN";
    }

    public interface WhereInStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereInStatement in(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new WhereInStatement("(" + placeholders + ")"), o);
        }

        default WhereInStatement in(int[] ids)
        {
            String placeholders = String.join(",", Collections.nCopies(ids.length, "?"));
            Object[] boxedIds = Arrays.stream(ids).boxed().toArray();
            return create(new WhereInStatement("(" + placeholders + ")"), boxedIds);
        }

        default WhereInStatement in(Collection<?> collection)
        {
            String placeholders = String.join(",", Collections.nCopies(collection.size(), "?"));
            return create(new WhereInStatement("(" + placeholders + ")"), collection);
        }

        default WhereInStatement in(FinalStatement finalStatement)
        {
            return create(new WhereInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
