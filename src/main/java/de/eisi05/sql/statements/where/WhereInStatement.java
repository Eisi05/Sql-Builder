package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

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
            return create(new WhereInStatement("(" + Arrays.stream(o).map(OrmUtils::formatValue).collect(Collectors.joining(",")) + ")"));
        }

        default WhereInStatement in(int[] ids)
        {
            String joined = Arrays.stream(ids)
                    .mapToObj(OrmUtils::formatValue)
                    .collect(Collectors.joining(","));
            return create(new WhereInStatement("(" + joined + ")"));
        }

        default WhereInStatement in(Collection<?> collection)
        {
            String joined = collection.stream()
                    .map(OrmUtils::formatValue)
                    .collect(Collectors.joining(","));
            return create(new WhereInStatement("(" + joined + ")"));
        }

        default WhereInStatement in(FinalStatement finalStatement)
        {
            return create(new WhereInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
