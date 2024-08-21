package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CaseNotConditionStatement extends CaseConditionStatement
{
    protected CaseNotConditionStatement(String query)
    {
        super(query);
    }

    public interface CaseNotConditionStatementContainer extends AbstractStatement.StatementContainer
    {
        default CaseNotConditionStatement notEqual(Object value)
        {
            return create(new CaseNotConditionStatement("<> " + value));
        }

        default CaseNotConditionStatement notEqual(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("<> (" + finalStatement.getQuery() + ")"));
        }

        default <T> CaseNotConditionStatement notBetween(T t1, T t2)
        {
            if(t1 instanceof String && t2 instanceof String)
                return create(new CaseNotConditionStatement("NOT BETWEEN '" + t1 + "' AND '" + t2 + "'"));

            return create(new CaseNotConditionStatement("NOT BETWEEN " + t1.toString() + " AND " + t2.toString()));
        }

        default CaseNotConditionStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(
                    new CaseNotConditionStatement(
                            "NOT BETWEEN (" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() +
                                    ")"));
        }

        default <T> CaseNotConditionStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            if(t2 instanceof String)
                return create(
                        new CaseNotConditionStatement(
                                "NOT BETWEEN (" + finalStatement1.getQuery() + ") AND '" + t2 + "'"));

            return create(
                    new CaseNotConditionStatement(
                            "NOT BETWEEN (" + finalStatement1.getQuery() + ") AND " + t2.toString()));
        }

        default CaseNotConditionStatement notGreaterThan(Object o)
        {
            CaseNotConditionStatement statement =
                    create(new CaseNotConditionStatement("> " + (o instanceof String ? "'" + o + "'" : o.toString())));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notGreaterThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement =
                    create(new CaseNotConditionStatement("> (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notIn(Object... o)
        {
            return create(new CaseNotConditionStatement("NOT IN (" +
                    Arrays.stream(o).map(o1 -> o1 instanceof String ? "'" + o1 + "'" : o1.toString())
                            .collect(Collectors.joining(",")) + ")"));
        }

        default CaseNotConditionStatement notIn(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("NOT IN (" + finalStatement.getQuery() + ")"));
        }

        default CaseNotConditionStatement isNotNull()
        {
            return create(new CaseNotConditionStatement("IS NOT NULL"));
        }

        default CaseNotConditionStatement notLessThan(Object o)
        {
            CaseNotConditionStatement statement =
                    create(new CaseNotConditionStatement("< " + (o instanceof String ? "'" + o + "'" : o.toString())));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notLessThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement =
                    create(new CaseNotConditionStatement("< (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notLike(Object o)
        {
            return create(
                    new CaseNotConditionStatement("NOT LIKE " + (o instanceof String ? "'" + o + "'" : o.toString())));
        }

        default CaseNotConditionStatement notLike(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("NOT LIKE (" + finalStatement.getQuery() + ")"));
        }
    }
}
