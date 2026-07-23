package de.eisi05.sql.utils;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.GeneratedValue;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.annotations.Table;
import org.postgresql.util.PGobject;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class providing Object-Relational Mapping (ORM) helper methods. Handles database table name resolution, ID extraction, object-to-column mappings, and
 * parameter serialization for query execution.
 */
public class OrmUtils
{
    /**
     * Shared Jackson object mapper instance used for JSON and JSONB serialization/deserialization tasks.
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Thread-safe cache storing parsed field configurations per entity class to improve reflection performance.
     */
    private static final Map<Class<?>, List<FieldConfig>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * Maps an entity object's fields to their corresponding database column names and values. Fields annotated with {@link GeneratedValue} are omitted from the
     * map. Object fields marked explicitly as {@code JSONB} are automatically serialized into PostgreSQL {@link PGobject} instances.
     *
     * @param object the entity object instance to parse
     * @return a map where keys are database column names and values are the mapped field values
     * @throws RuntimeException if a field is inaccessible or if JSONB serialization fails
     */
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
                Object value = config.field().get(object);

                Column column = config.field().getAnnotation(Column.class);
                if(value != null && column != null && "JSONB".equalsIgnoreCase(column.columnDefinition()))
                {
                    try
                    {
                        PGobject pgObject = new PGobject();
                        pgObject.setType("jsonb");
                        pgObject.setValue(OBJECT_MAPPER.writeValueAsString(value));
                        value = pgObject;
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException("Failed to serialize field to JSONB: " + config.field().getName(), e);
                    }
                }
                else
                    value = cleanParameter(value);

                map.put(config.columnName(), value);
            }
            catch(IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    /**
     * Extracts the value of the primary key field annotated with {@link Id} from the given entity instance.
     *
     * @param object the entity object instance
     * @return the value of the primary key field
     * @throws IllegalStateException if no field is annotated with {@code @Id}
     * @throws RuntimeException      if the primary key field is structurally inaccessible
     */
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

    /**
     * Resolves the database table name associated with the given class. Uses the name defined in the {@link Table} annotation, or falls back to the class
     * simple name if empty.
     *
     * @param clazz the entity class to inspect
     * @return the name of the database table
     * @throws IllegalArgumentException if the class is missing the {@code @Table} annotation
     */
    public static String resolveTable(Class<?> clazz)
    {
        Table table = clazz.getAnnotation(Table.class);

        if(table == null)
            throw new IllegalArgumentException("Missing @Table on " + clazz.getName());

        return table.name().isEmpty() ? clazz.getSimpleName() : table.name();
    }

    /**
     * Normalizes query parameter values into formats compatible with database drivers. Enums are converted to strings, and non-standard custom objects are
     * serialized to JSON strings. Standard Java API types (e.g., packages under {@code java.lang}, {@code java.math}, or {@code java.util}) are returned
     * as-is.
     *
     * @param value the raw parameter value to serialize or clean
     * @return the database-ready parameter representation
     */
    public static Object cleanParameter(Object value)
    {
        return switch(value)
        {
            case null -> null;
            case Enum<?> e -> e.name();
            case String s -> s;
            case PGobject pg -> pg;
            case LocalDateTime t -> t;
            case Timestamp t -> t;
            case Number n -> n;
            case Boolean b -> b;
            case UUID uuid -> uuid;
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

    /**
     * Internal container tracking a field's metadata configuration details.
     *
     * @param field       the reflective Java Field handler
     * @param columnName  the determined database column string name
     * @param isGenerated indicating if this value is automatically incremented or database-generated
     */
    private record FieldConfig(Field field, String columnName, boolean isGenerated) {}
}