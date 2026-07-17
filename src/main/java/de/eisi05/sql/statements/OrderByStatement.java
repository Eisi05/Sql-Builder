package de.eisi05.sql.statements;

import de.eisi05.sql.statements.Case.CaseFinalStatement;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Represents a SQL ORDER BY clause. Sorts the result set by one or more columns in ascending or descending order. Supports ordering by columns, aggregate
 * functions, and CASE expressions.
 */
public class OrderByStatement extends FinalStatement implements LimitStatement.LimitStatementContainer,
                                                                FetchFirstStatement.FetchFirstStatementContainer,
                                                                ForUpdateStatement.ForUpdateStatementContainer
{
    /**
     * Constructs a new OrderByStatement with the specified order objects.
     *
     * @param orderObjects the order specifications
     */
    private OrderByStatement(OrderObject... orderObjects)
    {
        super(Arrays.stream(orderObjects).map(orderObject -> orderObject.key + " " + orderObject.type.string)
                .collect(Collectors.joining(", ")));
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "ORDER BY"
     */
    @Override
    protected String getKey()
    {
        return "ORDER BY";
    }

    /**
     * Enumeration of sort order types.
     */
    public enum OrderType
    {
        /**
         * Ascending sort order.
         */
        ASCENDING("ASC"),
        /**
         * Descending sort order.
         */
        DESCENDING("DESC");

        /**
         * The SQL string representation.
         */
        private final String string;

        /**
         * Constructs an OrderType with the given SQL string.
         *
         * @param string the SQL string representation
         */
        OrderType(String string)
        {
            this.string = string;
        }
    }

    /**
     * Interface for containers that can create ORDER BY statements.
     */
    public interface OrderByStatementContainer extends StatementContainer
    {
        /**
         * Creates an ORDER BY statement for a single column in ascending order.
         *
         * @param key the column to order by
         * @return a new OrderByStatement
         */
        default OrderByStatement orderBy(String key)
        {
            return orderBy(key, OrderType.ASCENDING);
        }

        /**
         * Creates an ORDER BY statement for a single column with specified order.
         *
         * @param key  the column to order by
         * @param type the sort order type
         * @return a new OrderByStatement
         */
        default OrderByStatement orderBy(String key, OrderType type)
        {
            return create(new OrderByStatement(new OrderObject(key, type)));
        }

        /**
         * Creates an ORDER BY statement for a COUNT aggregate in ascending order.
         *
         * @param key the column to count
         * @return a new OrderByStatement
         */
        default OrderByStatement orderByCount(String key)
        {
            return orderByCount(key, OrderType.ASCENDING);
        }

        /**
         * Creates an ORDER BY statement for a COUNT aggregate with specified order.
         *
         * @param key  the column to count
         * @param type the sort order type
         * @return a new OrderByStatement
         */
        default OrderByStatement orderByCount(String key, OrderType type)
        {
            return create(new OrderByStatement(new OrderObject("COUNT(" + key + ")", type)));
        }

        /**
         * Creates an ORDER BY statement with multiple order specifications.
         *
         * @param orderObjects the order specifications
         * @return a new OrderByStatement
         */
        default OrderByStatement orderBy(OrderObject... orderObjects)
        {
            return create(new OrderByStatement(orderObjects));
        }

        /**
         * Creates an ORDER BY statement for a CASE expression in ascending order.
         *
         * @param caseFinalStatement the CASE expression
         * @return a new OrderByStatement
         */
        default OrderByStatement orderBy(CaseFinalStatement caseFinalStatement)
        {
            return orderBy(caseFinalStatement, OrderType.ASCENDING);
        }

        /**
         * Creates an ORDER BY statement for a CASE expression with specified order.
         *
         * @param caseFinalStatement the CASE expression
         * @param type               the sort order type
         * @return a new OrderByStatement
         */
        default OrderByStatement orderBy(CaseFinalStatement caseFinalStatement, OrderType type)
        {
            return orderBy(new OrderObject("(" + caseFinalStatement.getQuery() + ")", type));
        }
    }

    /**
     * Represents a column or expression with its sort order.
     *
     * @param key  the column or expression to order by
     * @param type the sort order type
     */
    public record OrderObject(String key, OrderType type)
    {
        /**
         * Creates an OrderObject with ascending order.
         *
         * @param key the column or expression
         */
        public OrderObject(String key)
        {
            this(key, OrderType.ASCENDING);
        }
    }
}
