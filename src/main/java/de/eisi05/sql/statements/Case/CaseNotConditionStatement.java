package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Collections;

/**
 * Represents negated conditions in a CASE WHEN expression. Extends CaseConditionStatement to support NOT versions of comparison operators. Some conditions like
 * notGreaterThan and notLessThan toggle the NOT flag on the parent.
 */
public class CaseNotConditionStatement extends CaseConditionStatement
{
    /**
     * Constructs a new CaseNotConditionStatement with the given condition.
     *
     * @param query the negated condition expression
     */
    protected CaseNotConditionStatement(String query)
    {
        super(query);
    }

    /**
     * Interface for containers that can create negated CASE condition statements.
     */
    public interface CaseNotConditionStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a not equal condition.
         *
         * @param value the value to compare
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notEqual(Object value)
        {
            return create(new CaseNotConditionStatement("<> ?"), value);
        }

        /**
         * Creates a not equal condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notEqual(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("<> (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates a not between condition.
         *
         * @param t1  the lower bound
         * @param t2  the upper bound
         * @param <T> the type of the bounds
         * @return a new CaseNotConditionStatement
         */
        default <T> CaseNotConditionStatement notBetween(T t1, T t2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN ? AND ?"), t1, t2);
        }

        /**
         * Creates a not between condition with subqueries.
         *
         * @param finalStatement1 the lower bound subquery
         * @param finalStatement2 the upper bound subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notBetween(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN (" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        /**
         * Creates a not between condition with a subquery and value.
         *
         * @param finalStatement1 the lower bound subquery
         * @param t2              the upper bound value
         * @param <T>             the type of the upper bound
         * @return a new CaseNotConditionStatement
         */
        default <T> CaseNotConditionStatement notBetween(FinalStatement finalStatement1, T t2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN (" + finalStatement1.getQuery() + ") AND ?"), t2);
        }

        /**
         * Creates a not greater than condition (less than or equal). Toggles the NOT flag on the parent CaseWhenStatement.
         *
         * @param o the value to compare
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notGreaterThan(Object o)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("> ?"), o);
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        /**
         * Creates a not greater than condition with a subquery. Toggles the NOT flag on the parent CaseWhenStatement.
         *
         * @param finalStatement the subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notGreaterThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("> (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        /**
         * Creates a not IN condition.
         *
         * @param o the values to check
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notIn(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new CaseNotConditionStatement("NOT IN (" + placeholders + ")"), o);
        }

        /**
         * Creates a not IN condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notIn(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("NOT IN (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates an IS NOT NULL condition.
         *
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement isNotNull()
        {
            return create(new CaseNotConditionStatement("IS NOT NULL"));
        }

        /**
         * Creates a not less than condition (greater than or equal). Toggles the NOT flag on the parent CaseWhenStatement.
         *
         * @param o the value to compare
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notLessThan(Object o)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("< ?"), o);
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        /**
         * Creates a not less than condition with a subquery. Toggles the NOT flag on the parent CaseWhenStatement.
         *
         * @param finalStatement the subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notLessThan(FinalStatement finalStatement)
        {
            CaseNotConditionStatement statement = create(new CaseNotConditionStatement("< (" + finalStatement.getQuery() + ")"));
            if(statement.parent instanceof CaseWhenStatement caseWhenStatement)
                caseWhenStatement.withNot = true;
            return statement;
        }

        /**
         * Creates a not LIKE condition.
         *
         * @param o the pattern to match
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notLike(Object o)
        {
            return create(new CaseNotConditionStatement("NOT LIKE ?"), o);
        }

        /**
         * Creates a not LIKE condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseNotConditionStatement
         */
        default CaseNotConditionStatement notLike(FinalStatement finalStatement)
        {
            return create(new CaseNotConditionStatement("NOT LIKE (" + finalStatement.getQuery() + ")"));
        }
    }
}
