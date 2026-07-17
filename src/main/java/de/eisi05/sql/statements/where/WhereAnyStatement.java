package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL condition involving the {@code ANY} (or {@code SOME}) quantifier combined with a relational operator.
 * <p>
 * Example: {@code field = ANY (subquery)}
 * </p>
 */
public class WhereAnyStatement extends FinalStatement
{
    /**
     * The relational operator combined with ANY (e.g., "=", "<>", ">").
     */
    private final String operator;

    /**
     * Constructs a {@code WhereAnyStatement} with the specified query fragment and operator.
     *
     * @param query    the SQL query or subquery fragment
     * @param operator the relational operator
     */
    protected WhereAnyStatement(String query, String operator)
    {
        super(query);
        this.operator = operator;
    }

    /**
     * Returns the SQL operator and keyword string for this statement.
     *
     * @return a string combining the operator and "ANY" (e.g., "= ANY")
     */
    @Override
    protected String getKey()
    {
        return operator + " ANY";
    }

    /**
     * A container interface providing fluent builder methods for {@code ANY} quantifier conditions.
     */
    public interface WhereAnyStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an equality condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching equals ANY
         */
        default WhereAnyStatement anyEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "="));
        }

        /**
         * Appends a non-equality condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching not equals ANY
         */
        default WhereAnyStatement anyNotEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<>"));
        }

        /**
         * Appends a greater-than condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than ANY
         */
        default WhereAnyStatement anyGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">"));
        }

        /**
         * Appends a greater-than-or-equal condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than or equal ANY
         */
        default WhereAnyStatement anyGreaterThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">="));
        }

        /**
         * Appends a less-than condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than ANY
         */
        default WhereAnyStatement anyLessThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<"));
        }

        /**
         * Appends a less-than-or-equal condition compared against ANY results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than or equal ANY
         */
        default WhereAnyStatement anyLessThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<="));
        }
    }
}