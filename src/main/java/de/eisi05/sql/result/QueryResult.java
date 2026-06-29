package de.eisi05.sql.result;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.PersistenceConstructor;
import de.eisi05.sql.interfaces.SqlDataType;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class QueryResult
{
    private final LinkedHashMap<String, Object> results;

    public QueryResult(ResultSet resultSet) throws SQLException
    {
        this.results = new LinkedHashMap<>();

        for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
        {
            if(SqlDataType.fromString(resultSet.getMetaData()
                    .getColumnTypeName(i)) instanceof SqlDataType.PrimitiveSqlDataType<?> primitiveSqlDataType)
            {
                results.put(resultSet.getMetaData().getColumnName(i),
                        resultSet.getObject(i, primitiveSqlDataType.getDataType()));
            }
            else
                results.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
        }
    }

    public <T> List<T> getObjects(SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return new LinkedList<>();

        return results.values().stream().map(o -> primitiveSqlDataType.getDataType().cast(o)).toList();
    }

    public <T> T get(String key, SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return null;

        return primitiveSqlDataType.getDataType().cast(results.getOrDefault(key, null));
    }

    public <T> T get(int column, SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return null;

        return primitiveSqlDataType.getDataType().cast(results.values().stream().toList().get(column));
    }

    public <T> T get(String key)
    {
        @SuppressWarnings("unchecked")
        T value = (T) results.getOrDefault(key, null);
        return value;
    }

    public <T> T get(int column)
    {
        @SuppressWarnings("unchecked")
        T value = (T) results.values().stream().toList().get(column);
        return value;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T map(Class<T> clazz)
    {
        try
        {
            if(clazz.isRecord())
            {
                var components = clazz.getRecordComponents();
                Class<?>[] paramTypes = Arrays.stream(components)
                        .map(java.lang.reflect.RecordComponent::getType)
                        .toArray(Class<?>[]::new);

                var constructor = clazz.getDeclaredConstructor(paramTypes);

                Object[] args = Arrays.stream(components)
                        .map(component ->
                        {
                            Column column = component.getAnnotation(Column.class);
                            String name = (column != null && !column.name().isEmpty()) ? column.name() : component.getName();
                            return mapValue(results.get(name), component.getType());
                        })
                        .toArray();

                constructor.setAccessible(true);
                return constructor.newInstance(args);
            }

            Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(c -> c.isAnnotationPresent(PersistenceConstructor.class))
                    .findFirst()
                    .orElseGet(() -> clazz.getDeclaredConstructors()[0]);

            Object[] args = Arrays.stream(constructor.getParameters())
                    .map(param ->
                    {
                        Column column = param.getAnnotation(Column.class);
                        String name = (column != null && !column.name().isEmpty()) ? column.name() : param.getName();
                        return mapValue(results.get(name), param.getType());
                    })
                    .toArray();

            constructor.setAccessible(true);
            return (T) constructor.newInstance(args);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Mapping failed for " + clazz.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    private Object mapValue(Object value, Class<?> targetType)
    {
        if(value == null)
        {
            if(targetType.isPrimitive())
            {
                if(targetType == int.class)
                    return 0;
                if(targetType == long.class)
                    return 0L;
                if(targetType == double.class)
                    return 0.0;
                if(targetType == float.class)
                    return 0.0f;
                if(targetType == boolean.class)
                    return false;
                if(targetType == byte.class)
                    return (byte) 0;
                if(targetType == short.class)
                    return (short) 0;
                if(targetType == char.class)
                    return '\u0000';
            }
            return null;
        }

        if(value instanceof String && targetType.isEnum())
            return Enum.valueOf((Class) targetType, (String) value);

        return value;
    }
}
