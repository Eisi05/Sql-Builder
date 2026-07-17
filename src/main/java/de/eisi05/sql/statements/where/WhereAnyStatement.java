package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL condition involving the <b>{@code ANY}</b> (or <b>{@code SOME}</b> ) quantifier combined with a relational operator.
 * <p>
 * Example: {@code field = ANY (subquery)}
 * </p>
 */
public class WhereAnyStatement extends FinalStatement
{
    /**
     * The relational operator combined with <b>{@code ANY}</b> (e.g., "=", "<>", ">").
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
     * A container interface providing fluent builder methods for <b>{@code ANY}</b> quantifier conditions.
     */
    public interface WhereAnyStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an equality condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching equals <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "="));
        }

        /**
         * Appends a non-equality condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching not equals <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyNotEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<>"));
        }

        /**
         * Appends a greater-than condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">"));
        }

        /**
         * Appends a greater-than-or-equal condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than or equal <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyGreaterThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">="));
        }

        /**
         * Appends a less-than condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyLessThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<"));
        }

        /**
         * Appends a less-than-or-equal condition compared against <b>{@code ANY}</b> results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than or equal <b>{@code ANY}</b>
         */
        default WhereAnyStatement anyLessThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<="));
        }
    }
}