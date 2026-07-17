package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.where.WhereEqualStatement;
import de.eisi05.sql.statements.where.WhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;

/**
 * Represents a SQL <b>{@code DELETE}</b> statement. Supports deleting from a table with optional <b>{@code WHERE}</b> conditions. Can also delete objects by
 * their ID using ORM annotations.
 */
public class DeleteStatement extends FinalStatement implements WhereStatement.WhereStatementContainer,
                                                               ExecuteUpdateStatement
{
    /**
     * Constructs a new DeleteStatement for the specified table.
     *
     * @param table the table to delete from
     */
    private DeleteStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "DELETE FROM"
     */
    @Override
    protected String getKey()
    {
        return "DELETE FROM";
    }

    /**
     * Interface for containers that can create <b>{@code DELETE}</b> statements.
     */
    public interface DeleteStatementContainer extends StatementContainer
    {
        /**
         * Creates a <b>{@code DELETE}</b> statement for the specified table.
         *
         * @param table the table to delete from
         * @return a new DeleteStatement
         */
        default DeleteStatement delete(String table)
        {
            return create(new DeleteStatement(table));
        }

        /**
         * Creates a <b>{@code DELETE}</b> statement for an object by its ID. Uses ORM annotations to determine the table and ID column.
         *
         * @param object the object to delete
         * @param <T>    the type of the object
         * @return a WhereEqualStatement with the <b>{@code DELETE}</b> and <b>{@code WHERE}</b> conditions
         */
        default <T> WhereEqualStatement delete(T object)
        {
            Class<?> clazz = object.getClass();

            String table = OrmUtils.resolveTable(clazz);
            Object id = OrmUtils.extractId(object);

            String idColumn = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(f ->
                    {
                        Column col = f.getAnnotation(Column.class);
                        return (col != null && !col.name().isEmpty()) ? col.name() : f.getName();
                    })
                    .orElseThrow();

            return create(delete(table))
                    .where(idColumn)
                    .equal(id);
        }
    }
}
