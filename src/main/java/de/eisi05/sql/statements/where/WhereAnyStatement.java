package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereAnyStatement extends FinalStatement
{
    private final String operator;

    protected WhereAnyStatement(String query, String operator)
    {
        super(query);
        this.operator = operator;
    }

    @Override
    protected String getKey()
    {
        return operator + " ANY";
    }

    public interface WhereAnyStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereAnyStatement anyEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "="));
        }

        default WhereAnyStatement anyNotEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<>"));
        }

        default WhereAnyStatement anyGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">"));
        }

        default WhereAnyStatement anyGreaterThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">="));
        }

        default WhereAnyStatement anyLessThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<"));
        }

        default WhereAnyStatement anyLessThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<="));
        }
    }
}
