package de.eisi05.sql.utils;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.GeneratedValue;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.annotations.Table;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrmUtils
{
    public static Map<String, Object> toColumnMap(Object object)
    {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = object.getClass();
        for(Field field : clazz.getDeclaredFields())
        {
            try
            {
                field.setAccessible(true);

                if (field.isAnnotationPresent(GeneratedValue.class))
                    continue;

                Column column = field.getAnnotation(Column.class);

                String name = column != null && !column.name().isEmpty() ? column.name() : field.getName();

                map.put(name, field.get(object));
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
            case String s -> "'" + s.replace("'", "''") + "'";
            case Enum<?> e -> "'" + e.name() + "'";
            case Boolean b -> b ? "1" : "0";
            default -> value.toString();
        };

    }
}
