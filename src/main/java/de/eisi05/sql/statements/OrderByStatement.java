package de.eisi05.sql.statements;

import de.eisi05.sql.statements.Case.CaseFinalStatement;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OrderByStatement extends FinalStatement implements LimitStatement.LimitStatementContainer,
        FetchFirstStatement.FetchFirstStatementContainer
{
    private OrderByStatement(OrderObject... orderObjects)
    {
        super(Arrays.stream(orderObjects).map(orderObject -> orderObject.key + " " + orderObject.type.string)
                .collect(Collectors.joining(", ")));
    }

    @Override
    protected String getKey()
    {
        return "ORDER BY";
    }

    public enum OrderType
    {
        ASCENDING("ASC"),
        DESCENDING("DESC");

        private final String string;

        OrderType(String string)
        {
            this.string = string;
        }
    }

    public interface OrderByStatementContainer extends StatementContainer
    {
        default OrderByStatement orderBy(String key)
        {
            return orderBy(key, OrderType.ASCENDING);
        }

        default OrderByStatement orderBy(String key, OrderType type)
        {
            return create(new OrderByStatement(new OrderObject(key, type)));
        }

        default OrderByStatement orderByCount(String key)
        {
            return orderByCount(key, OrderType.ASCENDING);
        }

        default OrderByStatement orderByCount(String key, OrderType type)
        {
            return create(new OrderByStatement(new OrderObject("COUNT(" + key + ")", type)));
        }

        default OrderByStatement orderBy(OrderObject... orderObjects)
        {
            return create(new OrderByStatement(orderObjects));
        }

        default OrderByStatement orderBy(CaseFinalStatement caseFinalStatement)
        {
            return orderBy(caseFinalStatement, OrderType.ASCENDING);
        }

        default OrderByStatement orderBy(CaseFinalStatement caseFinalStatement, OrderType type)
        {
            return orderBy(new OrderObject("(" + caseFinalStatement.getQuery() + ")", type));
        }
    }

    public record OrderObject(String key, OrderType type)
    {
        public OrderObject(String key)
        {
            this(key, OrderType.ASCENDING);
        }
    }
}
