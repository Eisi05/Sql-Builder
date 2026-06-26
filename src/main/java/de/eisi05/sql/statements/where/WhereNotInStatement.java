package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

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
            return create(new WhereNotInStatement("(" + Arrays.stream(o).map(OrmUtils::formatValue).collect(Collectors.joining(",")) + ")"));
        }

        default WhereNotInStatement notIn(FinalStatement finalStatement)
        {
            return create(new WhereNotInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
