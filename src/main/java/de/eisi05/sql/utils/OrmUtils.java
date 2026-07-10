package de.eisi05.sql.utils;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.GeneratedValue;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.annotations.Table;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static Object cleanParameter(Object value)
    {
        return switch(value)
        {
            case null -> null;
            case Enum<?> e -> e.name();
            case String s -> s;
            case java.time.LocalDateTime t -> t;
            case java.sql.Timestamp t -> t;
            case Number n -> n;
            case Boolean b -> b;
            case java.util.UUID uuid -> uuid;
            default ->
            {
                String className = value.getClass().getPackageName();
                if(className.startsWith("java.lang") || className.startsWith("java.math") || className.startsWith("java.util"))
                    yield value;

                try
                {
                    yield OBJECT_MAPPER.writeValueAsString(value);
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
