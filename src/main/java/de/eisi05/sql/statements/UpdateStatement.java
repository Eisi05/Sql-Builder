package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.Case.CaseStatement;
import de.eisi05.sql.statements.Case.CaseThenStatement;
import de.eisi05.sql.statements.Case.CaseWhenStatement;
import de.eisi05.sql.statements.where.AbstractWhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.*;

public class UpdateStatement extends AbstractStatement implements SetStatement.SetStatementContainer, ExecuteUpdateStatement
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

        default <T> AbstractWhereStatement update(T object, T... objects)
        {
            if(objects == null || objects.length == 0)
                return updateSingle(object);

            List<T> allObjects = new ArrayList<>();
            allObjects.add(object);
            allObjects.addAll(Arrays.asList(objects));

            Class<?> clazz = object.getClass();
            UpdateStatement updateStatement = new UpdateStatement(OrmUtils.resolveTable(clazz));

            String idColumn = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(f ->
                    {
                        Column col = f.getAnnotation(Column.class);
                        return (col != null && !col.name().isEmpty()) ? col.name() : f.getName();
                    })
                    .orElseThrow(() -> new IllegalStateException("Missing @Id-Column"));

            Set<String> columns = OrmUtils.toColumnMap(object).keySet();
            columns.remove(idColumn);

            Map<String, Object> caseValues = new LinkedHashMap<>();
            for(String column : columns)
            {
                CaseWhenStatement.CaseWhenStatementContainer caseStatement = CaseStatement.createCase(idColumn);
                for(T obj : allObjects)
                {
                    Object idVal = OrmUtils.extractId(obj);
                    Map<String, Object> objValues = OrmUtils.toColumnMap(obj);
                    Object colVal = objValues.get(column);

                    caseStatement = caseStatement.when(OrmUtils.formatValue(idVal)).thenValue(colVal);
                }

                caseValues.put(column, ((CaseThenStatement) caseStatement).elseReturn(column).end());
            }

            return create(updateStatement)
                    .set(caseValues)
                    .where(idColumn)
                    .in(allObjects.stream().map(OrmUtils::extractId).toList());
        }

        private <T> AbstractWhereStatement updateSingle(T object)
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

            return create(updateStatement)
                    .set(values)
                    .where(idColumn)
                    .equal(OrmUtils.formatValue(id));
        }
    }
}
