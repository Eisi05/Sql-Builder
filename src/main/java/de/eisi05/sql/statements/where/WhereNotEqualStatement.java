package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereNotEqualStatement extends WhereEqualStatement implements WhereNotStatement
{
    protected WhereNotEqualStatement(String query)
    {
        super(query);
    }

    @Override
    public boolean isNotAfterWhere()
    {
        return true;
    }

    public interface WhereNotEqualStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereNotEqualStatement notEqual(Object o)
        {
            return create(new WhereNotEqualStatement(o instanceof String ? "'" + o + "'" : o.toString()));
        }

        default WhereNotEqualStatement notEqual(FinalStatement finalStatement)
        {
            return create(new WhereNotEqualStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}
