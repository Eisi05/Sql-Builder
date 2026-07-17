package de.eisi05.sql.statements;

import de.eisi05.sql.statements.where.WhereStatement;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a SQL <b>{@code SET}</b> clause. Used in <b>{@code UPDATE}</b> statements to specify column values to be updated. Supports setting single or
 * multiple columns.
 */
public class SetStatement extends AbstractStatement implements WhereStatement.WhereStatementContainer, ReturningStatement.ReturningStatementContainer
{
    /**
     * Constructs a new SetStatement for a single column.
     *
     * @param column the column to set
     */
    private SetStatement(String column)
    {
        super(column + " = ?");
    }

    /**
     * Constructs a new SetStatement for multiple columns.
     *
     * @param setObjects the column-value pairs
     */
    private SetStatement(SetObject... setObjects)
    {
        super(Arrays.stream(setObjects).map(setObject -> setObject.column + " = ?").collect(Collectors.joining(", ")));
    }

    /**
     * Constructs a new SetStatement from a map of columns to values.
     *
     * @param setObjects the map of column names to values
     */
    private SetStatement(Map<String, Object> setObjects)
    {
        super(setObjects.keySet().stream().map(key -> key + " = ?").collect(Collectors.joining(", ")));
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "SET"
     */
    @Override
    protected String getKey()
    {
        return "SET";
    }

    /**
     * Interface for containers that can create <b>{@code SET}</b> statements.
     */
    public interface SetStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code SET}</b> statement for a single column.
         *
         * @param column the column to set
         * @param value  the value to set
         * @return a new SetStatement
         */
        default SetStatement set(String column, Object value)
        {
            return create(new SetStatement(column), value);
        }

        /**
         * Creates a <b>{@code SET}</b> statement for multiple columns.
         *
         * @param setObjects the column-value pairs
         * @return a new SetStatement
         */
        default SetStatement set(SetObject... setObjects)
        {
            return create(new SetStatement(setObjects), Arrays.stream(setObjects).map(setObject -> setObject.value).toList());
        }

        /**
         * Creates a <b>{@code SET}</b> statement from a map of columns to values.
         *
         * @param objects the map of column names to values
         * @return a new SetStatement
         */
        default SetStatement set(Map<String, Object> objects)
        {
            return create(new SetStatement(objects), objects.values());
        }
    }

    /**
     * Represents a column-value pair for the <b>{@code SET}</b> clause.
     *
     * @param column the column name
     * @param value  the value to set
     */
    public record SetObject(String column, Object value)
    {
    }
}
