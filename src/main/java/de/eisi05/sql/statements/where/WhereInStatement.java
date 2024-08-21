package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
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
            return create(new WhereInStatement("(" +
                    Arrays.stream(o).map(o1 -> o1 instanceof String ? "'" + o1 + "'" : o1.toString())
                            .collect(Collectors.joining(",")) + ")"));
        }

        default WhereInStatement in(FinalStatement finalStatement)
        {
            return create(new WhereInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
