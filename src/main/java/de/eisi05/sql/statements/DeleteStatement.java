package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.where.WhereEqualStatement;
import de.eisi05.sql.statements.where.WhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;

public class DeleteStatement extends FinalStatement implements WhereStatement.WhereStatementContainer,
                                                               ExecuteUpdateStatement
{
    private DeleteStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "DELETE FROM";
    }

    public interface DeleteStatementContainer extends StatementContainer
    {
        default DeleteStatement delete(String table)
        {
            return create(new DeleteStatement(table));
        }

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
