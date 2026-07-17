package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents an SQL condition involving the {@code ALL} quantifier combined with a relational operator.
 * <p>
 * Example: {@code field > ALL (subquery)}
 * </p>
 */
public class WhereAllStatement extends FinalStatement
{
    /**
     * The relational operator combined with ALL (e.g., "=", "<>", ">").
     */
    private final String operator;

    /**
     * Constructs a {@code WhereAllStatement} with the specified query fragment and operator.
     *
     * @param query    the SQL query or subquery fragment
     * @param operator the relational operator
     */
    protected WhereAllStatement(String query, String operator)
    {
        super(query);
        this.operator = operator;
    }

    /**
     * Returns the SQL operator and keyword string for this statement.
     *
     * @return a string combining the operator and "ALL" (e.g., "= ALL")
     */
    @Override
    protected String getKey()
    {
        return operator + " ALL";
    }

    /**
     * A container interface providing fluent builder methods for {@code ALL} quantifier conditions.
     */
    public interface WhereAllStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an equality condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching equals ALL
         */
        default WhereAnyStatement allEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "="));
        }

        /**
         * Appends a non-equality condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching not equals ALL
         */
        default WhereAnyStatement allNotEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<>"));
        }

        /**
         * Appends a greater-than condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than ALL
         */
        default WhereAnyStatement allGreaterThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">"));
        }

        /**
         * Appends a greater-than-or-equal condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching greater than or equal ALL
         */
        default WhereAnyStatement allGreaterThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", ">="));
        }

        /**
         * Appends a less-than condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than ALL
         */
        default WhereAnyStatement allLessThan(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<"));
        }

        /**
         * Appends a less-than-or-equal condition compared against ALL results of a subquery.
         *
         * @param finalStatement the subquery statement
         * @return a new {@link WhereAnyStatement} matching less than or equal ALL
         */
        default WhereAnyStatement allLessThanOrEquals(FinalStatement finalStatement)
        {
            return create(new WhereAnyStatement("(" + finalStatement.getQuery() + ")", "<="));
        }
    }
}