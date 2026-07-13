package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
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

            return update(allObjects);
        }

        default <T> AbstractWhereStatement update(Collection<T> objects)
        {
            T first = objects.iterator().next();
            Class<?> clazz = first.getClass();
            String idColumn = Arrays.stream(clazz.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findFirst()
                    .map(f ->
                    {
                        Column col = f.getAnnotation(Column.class);
                        return (col != null && !col.name().isEmpty()) ? col.name() : f.getName();
                    })
                    .orElseThrow(() -> new IllegalStateException("Missing @Id-Column"));

            UpdateStatement updateStatement = new UpdateStatement(OrmUtils.resolveTable(clazz));
            Map<String, Object> baseValues = OrmUtils.toColumnMap(first);

            List<String> columnsOrder = new ArrayList<>(baseValues.keySet());
            columnsOrder.remove(idColumn);

            List<List<Object>> batchParameters = new ArrayList<>();
            for(T obj : objects)
            {
                Map<String, Object> objValues = OrmUtils.toColumnMap(obj);
                List<Object> paramsForObj = new ArrayList<>();

                for(String col : columnsOrder)
                    paramsForObj.add(objValues.get(col));
                paramsForObj.add(OrmUtils.extractId(obj));

                batchParameters.add(paramsForObj);
            }

            Map<String, Object> setTemplate = new LinkedHashMap<>();
            for(String col : columnsOrder)
                setTemplate.put(col, baseValues.get(col));

            AbstractWhereStatement builtStatement = create(updateStatement)
                    .set(setTemplate)
                    .where(idColumn)
                    .equal(OrmUtils.extractId(first));

            builtStatement.batchParameters.addAll(batchParameters);
            return builtStatement;
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
                    .equal(id);
        }
    }
}
