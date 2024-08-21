package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CaseConditionStatement extends AbstractStatement
        implements CaseThenStatementContainer, CaseNextConditionStatementContainer
{
    protected CaseConditionStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "";
    }

    public interface CaseConditionStatementContainer extends StatementContainer
    {
        default CaseConditionStatement equal(Object value)
        {
            return create(new CaseConditionStatement(" = " + value));
        }

        default CaseConditionStatement equal(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement(" = (" + finalStatement.getQuery() + ")"));
        }

        default <T> CaseConditionStatement between(T t1, T t2)
        {
            if(t1 instanceof String && t2 instanceof String)
                return create(new CaseConditionStatement("BETWEEN '" + t1 + "' AND '" + t2 + "'"));

            return create(new CaseConditionStatement("BETWEEN " + t1.toString() + " AND " + t2.toString()));
        }

        default CaseConditionStatement between(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(
                    new CaseConditionStatement(
                            "BETWEEN (" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> CaseConditionStatement between(FinalStatement finalStatement1, T t2)
        {
            if(t2 instanceof String)
                return create(
                        new CaseConditionStatement("BETWEEN (" + finalStatement1.getQuery() + ") AND '" + t2 + "'"));

            return create(
                    new CaseConditionStatement("BETWEEN (" + finalStatement1.getQuery() + ") AND " + t2.toString()));
        }

        default CaseConditionStatement greaterThanOrEqual(Object o)
        {
            return create(new CaseConditionStatement(">= " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseConditionStatement greaterThanOrEqual(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement(">= (" + finalStatement.getQuery() + ")"));
        }

        default CaseConditionStatement greaterThan(Object o)
        {
            return create(new CaseConditionStatement("> " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseConditionStatement greaterThan(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("> (" + finalStatement.getQuery() + ")"));
        }

        default CaseConditionStatement in(Object... o)
        {
            return create(new CaseConditionStatement("IN (" +
                    Arrays.stream(o).map(o1 -> o1 instanceof String ? "'" + o1 + "'" : o1.toString())
                            .collect(Collectors.joining(",")) + ")"));
        }

        default CaseConditionStatement in(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("IN (" + finalStatement.getQuery() + ")"));
        }

        default CaseConditionStatement isNull()
        {
            return create(new CaseConditionStatement("IS NULL"));
        }

        default CaseConditionStatement lessThanOrEqual(Object o)
        {
            return create(new CaseConditionStatement("<= " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseConditionStatement lessThanOrEqual(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("<= (" + finalStatement.getQuery() + ")"));
        }

        default CaseConditionStatement lessThan(Object o)
        {
            return create(new CaseConditionStatement("< " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseConditionStatement lessThan(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("< (" + finalStatement.getQuery() + ")"));
        }

        default CaseConditionStatement like(Object o)
        {
            return create(new CaseConditionStatement("LIKE " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseConditionStatement like(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("LIKE (" + finalStatement.getQuery() + ")"));
        }
    }
}
