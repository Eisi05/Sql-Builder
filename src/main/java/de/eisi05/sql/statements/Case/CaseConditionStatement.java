package de.eisi05.sql.statements.Case;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Collections;

/**
 * Represents a condition in a CASE WHEN expression. Supports various comparison operators like equal, between, greater than, less than, etc. Can be followed by
 * THEN or additional conditions with AND/OR.
 */
public class CaseConditionStatement extends AbstractStatement
        implements CaseThenStatement.CaseThenStatementContainer, CaseNextConditionStatementContainer
{
    /**
     * Constructs a new CaseConditionStatement with the given condition.
     *
     * @param query the condition expression
     */
    protected CaseConditionStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since conditions don't have a keyword prefix.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create CASE condition statements.
     */
    public interface CaseConditionStatementContainer extends StatementContainer
    {
        /**
         * Creates an equal condition.
         *
         * @param value the value to compare
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement equal(Object value)
        {
            return create(new CaseConditionStatement(" = ?"), value);
        }

        /**
         * Creates an equal condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement equal(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement(" = (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates a between condition.
         *
         * @param t1  the lower bound
         * @param t2  the upper bound
         * @param <T> the type of the bounds
         * @return a new CaseConditionStatement
         */
        default <T> CaseConditionStatement between(T t1, T t2)
        {
            return create(new CaseNotConditionStatement("NOT BETWEEN ? AND ?"), t1, t2);
        }

        /**
         * Creates a between condition with subqueries.
         *
         * @param finalStatement1 the lower bound subquery
         * @param finalStatement2 the upper bound subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement between(FinalStatement finalStatement1, FinalStatement finalStatement2)
        {
            return create(new CaseConditionStatement("BETWEEN (" + finalStatement1.getQuery() + ") AND (" + finalStatement2.getQuery() + ")"));
        }

        /**
         * Creates a between condition with a subquery and value.
         *
         * @param finalStatement1 the lower bound subquery
         * @param t2              the upper bound value
         * @param <T>             the type of the upper bound
         * @return a new CaseConditionStatement
         */
        default <T> CaseConditionStatement between(FinalStatement finalStatement1, T t2)
        {
            return create(new CaseConditionStatement("BETWEEN (" + finalStatement1.getQuery() + ") AND ?"), t2);
        }

        /**
         * Creates a greater than or equal condition.
         *
         * @param o the value to compare
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement greaterThanOrEqual(Object o)
        {
            return create(new CaseConditionStatement(">= ?"), o);
        }

        /**
         * Creates a greater than or equal condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement greaterThanOrEqual(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement(">= (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates a greater than condition.
         *
         * @param o the value to compare
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement greaterThan(Object o)
        {
            return create(new CaseConditionStatement("> ?"), o);
        }

        /**
         * Creates a greater than condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement greaterThan(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("> (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates an IN condition.
         *
         * @param o the values to check
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement in(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new CaseConditionStatement("IN (" + placeholders + ")"), o);
        }

        /**
         * Creates an IN condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement in(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("IN (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates an IS NULL condition.
         *
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement isNull()
        {
            return create(new CaseConditionStatement("IS NULL"));
        }

        /**
         * Creates a less than or equal condition.
         *
         * @param o the value to compare
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement lessThanOrEqual(Object o)
        {
            return create(new CaseConditionStatement("<= ?"), o);
        }

        /**
         * Creates a less than or equal condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement lessThanOrEqual(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("<= (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates a less than condition.
         *
         * @param o the value to compare
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement lessThan(Object o)
        {
            return create(new CaseConditionStatement("< ?"), o);
        }

        /**
         * Creates a less than condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement lessThan(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("< (" + finalStatement.getQuery() + ")"));
        }

        /**
         * Creates a LIKE condition.
         *
         * @param o the pattern to match
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement like(Object o)
        {
            return create(new CaseConditionStatement("LIKE ?"), o);
        }

        /**
         * Creates a LIKE condition with a subquery.
         *
         * @param finalStatement the subquery
         * @return a new CaseConditionStatement
         */
        default CaseConditionStatement like(FinalStatement finalStatement)
        {
            return create(new CaseConditionStatement("LIKE (" + finalStatement.getQuery() + ")"));
        }
    }
}
