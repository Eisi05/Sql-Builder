package de.eisi05.sql.utils;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.GeneratedValue;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.annotations.Table;
import de.eisi05.sql.statements.FinalStatement;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrmUtils
{
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Map<Class<?>, List<FieldConfig>> FIELD_CACHE = new ConcurrentHashMap<>();

    public static Map<String, Object> toColumnMap(Object object)
    {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = object.getClass();

        List<FieldConfig> configs = FIELD_CACHE.computeIfAbsent(clazz, clz ->
                Arrays.stream(clz.getDeclaredFields()).map(field ->
                {
                    field.setAccessible(true);
                    Column column = field.getAnnotation(Column.class);
                    String name = column != null && !column.name().isEmpty() ? column.name() : field.getName();
                    boolean isGenerated = field.isAnnotationPresent(GeneratedValue.class);
                    return new FieldConfig(field, name, isGenerated);
                }).toList());

        for(FieldConfig config : configs)
        {
            if(config.isGenerated())
                continue;

            try
            {
                map.put(config.columnName(), config.field().get(object));
            }
            catch(IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    public static Object extractId(Object object)
    {
        Class<?> clazz = object.getClass();
        for(Field field : clazz.getDeclaredFields())
        {
            if(field.isAnnotationPresent(Id.class))
            {
                try
                {
                    field.setAccessible(true);
                    return field.get(object);
                }
                catch(IllegalAccessException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new IllegalStateException("No @Id found in " + clazz.getName());
    }

    public static String resolveTable(Class<?> clazz)
    {
        Table table = clazz.getAnnotation(Table.class);

        if(table == null)
            throw new IllegalArgumentException("Missing @Table on " + clazz.getName());

        return table.name().isEmpty() ? clazz.getSimpleName() : table.name();
    }

    public static String formatValue(Object value)
    {
        return switch(value)
        {
            case null -> "NULL";
            case FinalStatement statement ->
            {
                String query = statement.getQuery();
                yield query.substring(0, query.length() - 1);
            }
            case String s -> "'" + s.replace("'", "''") + "'";
            case Enum<?> e -> "'" + e.name() + "'";
            case Timestamp t -> "'" + t + "'";
            case LocalDateTime t -> "'" + t + "'";
            case Number n -> n.toString();
            case Boolean b -> b.toString();
            case UUID uuid -> "'" + uuid + "'";
            case int[] primitiveInts -> "'{" + Arrays.stream(primitiveInts)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(",")) + "}'";
            case long[] primitiveLongs -> "'{" + Arrays.stream(primitiveLongs)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(",")) + "}'";
            case Object[] objectArray -> "'{" + Arrays.stream(objectArray)
                    .map(Object::toString)
                    .collect(Collectors.joining(",")) + "}'";
            default ->
            {
                String className = value.getClass().getPackageName();
                if(className.startsWith("java.lang") || className.startsWith("java.math") || className.startsWith("java.util"))
                    yield value.toString();

                try
                {
                    String json = OBJECT_MAPPER.writeValueAsString(value);
                    yield "'" + json.replace("'", "''") + "'::jsonb";
                }
                catch(Exception e)
                {
                    yield value.toString();
                }
            }
        };
    }

    private record FieldConfig(Field field, String columnName, boolean isGenerated) {}
}
