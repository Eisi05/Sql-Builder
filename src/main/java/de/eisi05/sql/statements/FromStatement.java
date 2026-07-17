package de.eisi05.sql.statements;

import de.eisi05.sql.statements.join.JoinStatementContainer;
import de.eisi05.sql.statements.union.UnionStatementContainer;
import de.eisi05.sql.statements.where.WhereExistsStatement;
import de.eisi05.sql.statements.where.WhereStatement;

import java.util.Arrays;

/**
 * Represents a SQL <b>{@code FROM}</b> clause. Specifies the table(s) to query from in a <b>{@code SELECT}</b> statement. Supports joining, ordering, grouping,
 * and other follow-up operations.
 */
public class FromStatement extends FinalStatement implements WhereStatement.WhereStatementContainer,
                                                             JoinStatementContainer, UnionStatementContainer, OrderByStatement.OrderByStatementContainer,
                                                             GroupByStatement.GroupByStatementContainer, WhereExistsStatement.WhereExistsStatementContainer,
                                                             FetchFirstStatement.FetchFirstStatementContainer,
                                                             ForUpdateStatement.ForUpdateStatementContainer,
                                                             ReturningStatement.ReturningStatementContainer
{
    /**
     * Constructs a new FromStatement for the specified tables.
     *
     * @param tables the table names to query from
     */
    private FromStatement(String... tables)
    {
        super(String.join(", ", tables));
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "FROM"
     */
    @Override
    protected String getKey()
    {
        return "FROM";
    }

    /**
     * Interface for containers that can create <b>{@code FROM}</b> statements.
     */
    public interface FromStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code FROM}</b> statement for the specified tables.
         *
         * @param tables the table names to query from
         * @return a new FromStatement
         */
        default FromStatement from(String... tables)
        {
            return create(new FromStatement(tables));
        }

        /**
         * Creates a <b>{@code FROM}</b> statement using AsStatementObject keys.
         *
         * @param keys the AsStatementObject keys
         * @return a new FromStatement
         */
        default FromStatement from(AsStatementObject... keys)
        {
            return create(new FromStatement(Arrays.stream(keys)
                    .map(AsStatementObject::getKey)
                    .toList().toArray(new String[0])));
        }

        /**
         * Creates a <b>{@code FROM}</b> statement using final alias objects.
         *
         * @param keys the FinalAsStatementObject keys with aliases
         * @return a new FromStatement
         */
        default FromStatement from(AsStatementObject.FinalAsStatementObject... keys)
        {
            return create(new FromStatement(Arrays.stream(keys)
                    .map(selectObject -> selectObject.key() +
                            (selectObject.as() != null ? " AS " + selectObject.as() : ""))
                    .toList().toArray(new String[0])));
        }
    }
}
