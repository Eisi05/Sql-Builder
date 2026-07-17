package de.eisi05.sql.result;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.PersistenceConstructor;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.utils.OrmUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Wraps a single database row result map, providing extraction routines and mapping mechanics to convert database rows directly into Java Objects, Records, or
 * primitives.
 */
public class QueryResult
{
    private final LinkedHashMap<String, Object> results;

    /**
     * Constructs a {@code QueryResult} entity container from the current row context of a JDBC {@link ResultSet}.
     *
     * @param resultSet the active database result cursor
     * @throws SQLException if a data access configuration error occurs
     */
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

    /**
     * Collects and casts all row elements into a sequential linked list.
     *
     * @param dataType the target primitive structure type
     * @param <T>      the matching element casting boundary type
     * @return a list of typed values mapped from the result columns
     */
    public <T> List<T> getObjects(SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return new LinkedList<>();

        return results.values().stream().map(o -> primitiveSqlDataType.getDataType().cast(o)).toList();
    }

    /**
     * Extracts a column value by its field name label identifier and casts it safely.
     *
     * @param key      the database column name label
     * @param dataType the validation structure type mapping
     * @param <T>      the matching casting boundary type
     * @return the typed row cell value, or {@code null} if missing
     */
    public <T> T get(String key, SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return null;

        return primitiveSqlDataType.getDataType().cast(results.getOrDefault(key, null));
    }

    /**
     * Extracts a column value by its absolute index position and casts it safely.
     *
     * @param column   the zero-indexed order column location
     * @param dataType the validation structure type mapping
     * @param <T>      the matching casting boundary type
     * @return the typed row cell value, or {@code null} if missing
     */
    public <T> T get(int column, SqlDataType<T> dataType)
    {
        if(!(dataType instanceof SqlDataType.PrimitiveSqlDataType<T> primitiveSqlDataType))
            return null;

        return primitiveSqlDataType.getDataType().cast(results.values().stream().toList().get(column));
    }

    /**
     * Extracts an unchecked loosely typed column value by its label identifier.
     *
     * @param key the database column name label
     * @param <T> the expected casting type
     * @return the row value entry
     */
    public <T> T get(String key)
    {
        @SuppressWarnings("unchecked")
        T value = (T) results.getOrDefault(key, null);
        return value;
    }

    /**
     * Extracts an unchecked loosely typed column value by its absolute index position.
     *
     * @param column the zero-indexed order column location
     * @param <T>    the expected casting type
     * @return the row value entry
     */
    public <T> T get(int column)
    {
        @SuppressWarnings("unchecked")
        T value = (T) results.values().stream().toList().get(column);
        return value;
    }

    /**
     * Maps the columns from this row into a fresh instance of the specified class. Handles reflections over standard POJOs (using constructor strategies and
     * fields) as well as Records. Honors annotation attributes such as {@link Column} and {@link PersistenceConstructor}.
     *
     * @param clazz the target class blueprint to populate
     * @param <T>   the runtime mapping class model parameter type
     * @return a fully populated object instance
     * @throws RuntimeException if reflecting or converting types runs into errors
     */
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
                            return mapValue(results.get(name), component.getType(), component.getGenericType());
                        })
                        .toArray();

                constructor.setAccessible(true);
                return constructor.newInstance(args);
            }

            Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(c -> c.isAnnotationPresent(PersistenceConstructor.class))
                    .findFirst()
                    .orElseGet(() -> Arrays.stream(clazz.getDeclaredConstructors())
                            .filter(constructor1 -> constructor1.getParameterCount() > 0)
                            .findFirst()
                            .orElse(getClass().getDeclaredConstructors()[0]));

            var parameters = constructor.getParameters();
            var declaredFields = clazz.getDeclaredFields();
            Object[] args = new Object[parameters.length];

            for(int i = 0; i < parameters.length; i++)
            {
                var param = parameters[i];
                Column column = param.getAnnotation(Column.class);
                String name = null;

                if(column != null && !column.name().isEmpty())
                    name = column.name();
                else
                {
                    try
                    {
                        Field field = clazz.getDeclaredField(param.getName());
                        Column fieldColumn = field.getAnnotation(Column.class);
                        name = (fieldColumn != null && !fieldColumn.name().isEmpty()) ? fieldColumn.name() : field.getName();
                    }
                    catch(NoSuchFieldException e)
                    {
                        if(i < declaredFields.length)
                        {
                            Field field = declaredFields[i];
                            Column fieldColumn = field.getAnnotation(Column.class);
                            name = (fieldColumn != null && !fieldColumn.name().isEmpty()) ? fieldColumn.name() : field.getName();
                        }
                    }
                }

                if(name == null)
                    name = param.getName();

                var result = results.get(name);
                args[i] = mapValue(result, param.getType(), param.getParameterizedType());
            }

            constructor.setAccessible(true);
            return (T) constructor.newInstance(args);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Mapping failed for " + clazz.getName(), e);
        }
    }

    /**
     * Converts raw relational database values into complex object fields, handling null default primitives, time representations, enums, SQL arrays, and
     * Jackson-mapped JSON data blocks.
     */
    @SuppressWarnings("unchecked")
    private Object mapValue(Object value, Class<?> targetType, java.lang.reflect.Type genericType)
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

        if(value instanceof java.sql.Timestamp ts && targetType == LocalDateTime.class)
            return ts.toLocalDateTime();

        if(value instanceof java.sql.Date date && targetType == java.time.LocalDate.class)
            return date.toLocalDate();

        if(value instanceof String && targetType.isEnum())
            return Enum.valueOf((Class) targetType, (String) value);

        if(value instanceof java.sql.Array sqlArray)
        {
            try
            {
                Object nativeArray = sqlArray.getArray();
                if(List.class.isAssignableFrom(targetType) && nativeArray != null)
                    return Arrays.asList((Object[]) nativeArray);

                return nativeArray;
            }
            catch(java.sql.SQLException e)
            {
            }
        }

        if((targetType.isRecord() || List.class.isAssignableFrom(targetType)) && !(value instanceof List))
        {
            try
            {
                String jsonString = value.toString();
                return OrmUtils.OBJECT_MAPPER.readValue(jsonString, OrmUtils.OBJECT_MAPPER.getTypeFactory().constructType(genericType));
            }
            catch(Exception e)
            {
            }
        }

        if((targetType.isRecord() || Map.class.isAssignableFrom(targetType)) && !(value instanceof Map<?, ?>))
        {
            try
            {
                String jsonString = value.toString();
                return OrmUtils.OBJECT_MAPPER.readValue(jsonString, OrmUtils.OBJECT_MAPPER.getTypeFactory().constructType(genericType));
            }
            catch(Exception e)
            {
            }
        }

        return value;
    }
}