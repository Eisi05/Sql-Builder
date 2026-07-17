package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.where.AbstractWhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.*;

/**
 * Represents a SQL UPDATE statement. Supports updating table rows with optional WHERE conditions. Can update single objects or multiple objects using ORM
 * annotations. Supports batch updates for multiple objects.
 */
public class UpdateStatement extends AbstractStatement implements SetStatement.SetStatementContainer, ExecuteUpdateStatement
{
    /**
     * Constructs a new UpdateStatement for the specified table.
     *
     * @param table the table to update
     */
    private UpdateStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "UPDATE"
     */
    @Override
    protected String getKey()
    {
        return "UPDATE";
    }

    /**
     * Interface for containers that can create UPDATE statements.
     */
    public interface UpdateStatementContainer extends StatementContainer
    {
        /**
         * Creates an UPDATE statement for the specified table.
         *
         * @param table the table to update
         * @return a new UpdateStatement
         */
        default UpdateStatement update(String table)
        {
            return create(new UpdateStatement(table));
        }

        /**
         * Creates an UPDATE statement for one or more objects by their ID. Uses ORM annotations to determine the table, columns, and ID column. Supports batch
         * updates when multiple objects are provided.
         *
         * @param object  the first object to update
         * @param objects additional objects to update (optional)
         * @param <T>     the type of the objects
         * @return an AbstractWhereStatement with the UPDATE, SET, and WHERE conditions
         */
        default <T> AbstractWhereStatement update(T object, T... objects)
        {
            if(objects == null || objects.length == 0)
                return updateSingle(object);

            List<T> allObjects = new ArrayList<>();
            allObjects.add(object);
            allObjects.addAll(Arrays.asList(objects));

            return update(allObjects);
        }

        /**
         * Creates a batch UPDATE statement for a collection of objects. Uses ORM annotations to determine the table, columns, and ID column. All objects must
         * be of the same type.
         *
         * @param objects the collection of objects to update
         * @param <T>     the type of the objects
         * @return an AbstractWhereStatement with the UPDATE, SET, and WHERE conditions
         * @throws IllegalStateException if the @Id annotation is missing
         */
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

        /**
         * Creates a single UPDATE statement for an object by its ID. Uses ORM annotations to determine the table, columns, and ID column.
         *
         * @param object the object to update
         * @param <T>    the type of the object
         * @return an AbstractWhereStatement with the UPDATE, SET, and WHERE conditions
         */
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
