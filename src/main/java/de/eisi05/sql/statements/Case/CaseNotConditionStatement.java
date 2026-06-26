package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

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
            return create(new CaseNotConditionStatement("<> " + OrmUtils.formatValue(value)));
        }

        default CaseNotConditionStatement notEqual(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("<> (" + finalStatement.getQuery() + ")"));
        }

        default <T> CaseNotConditionStatement notBetween(T t1, T t2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN " + OrmUtils.formatValue(t1) + " AND " + OrmUtils.formatValue(t2)));
        }

        default CaseNotConditionStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN (" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        default <T> CaseNotConditionStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN (" + finalStatement1.getQuery() + ") AND " + OrmUtils.formatValue(t2)));
        }

        default CaseNotConditionStatement notGreaterThan(Object o)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("> " + OrmUtils.formatValue(o)));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notGreaterThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("> (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notIn(Object... o)
        {
            return create(new CaseNotConditionStatement("NOT IN (" + Arrays.stream(o).map(OrmUtils::formatValue).collect(Collectors.joining(",")) + ")"));
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
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("< " + OrmUtils.formatValue(o)));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notLessThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("< (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        default CaseNotConditionStatement notLike(Object o)
        {
            return create(new CaseNotConditionStatement("NOT LIKE " + OrmUtils.formatValue(o)));
        }

        default CaseNotConditionStatement notLike(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("NOT LIKE (" + finalStatement.getQuery() + ")"));
        }
    }
}
