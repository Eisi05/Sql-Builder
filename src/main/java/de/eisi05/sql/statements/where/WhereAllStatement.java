package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class WhereAllStatement extends FinalStatement
{
    private final String operator;

    protected WhereAllStatement(String query, String operator)
    {
        super(query);
        this.operator = operator;
    }

    @Override
    protected String getKey()
    {
        return operator + " ALL";
    }

    public interface WhereAllStatementContainer extends AbstractStatement.StatementContainer
    {
        default WhereAnyStatement allEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "="));
        }

        default WhereAnyStatement allNotEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<>"));
        }

        default WhereAnyStatement allGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">"));
        }

        default WhereAnyStatement allGreaterThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">="));
        }

        default WhereAnyStatement allLessThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<"));
        }

        default WhereAnyStatement allLessThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<="));
        }
    }
}
