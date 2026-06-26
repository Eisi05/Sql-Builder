package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.where.WhereEqualStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;
import java.util.Map;

public class UpdateStatement extends AbstractStatement implements SetStatement.SetStatementContainer,
                                                                  ExecuteUpdateStatement
{
    private UpdateStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "UPDATE";
    }

    public interface UpdateStatementContainer extends StatementContainer
    {
        default UpdateStatement update(String table)
        {
            return create(new UpdateStatement(table));
        }

        default <T> WhereEqualStatement update(T object)
        {
            UpdateStatement updateStatement = new UpdateStatement(OrmUtils.resolveTable(object.getClass()));
            Map<String, Object> values = OrmUtils.toColumnMap(object);

            Object id = OrmUtils.extractId(object);

            String idColumn = Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(f ->
                    {
                        Column col = f.getAnnotation(Column.class);
                        return (col != null && !col.name().isEmpty()) ? col.name() : f.getName();
                    })
                    .orElseThrow();

            return create(updateStatement).set(values)
                    .where(idColumn)
                    .equal(OrmUtils.formatValue(id));
        }
    }
}
