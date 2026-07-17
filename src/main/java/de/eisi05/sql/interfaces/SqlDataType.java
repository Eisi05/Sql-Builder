package de.eisi05.sql.interfaces;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.statements.table.TableColumn;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents SQL data types mapped into Java types. Contains predefined static types as well as helper factory methods to construct or resolve mappings.
 *
 * @param <T> the corresponding Java type for the SQL data type
 */
public interface SqlDataType<T>
{
    // Predefined Standard Data Types
    SqlDataType<Blob> TINYBLOB = new PrimitiveSqlDataType<>(Blob.class);
    SqlDataType<String> TINYTEXT = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<String> MEDIUMTEXT = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<Blob> MEDIUMBLOB = new PrimitiveSqlDataType<>(Blob.class);
    SqlDataType<String> LONGTEXT = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<Blob> LONGBLOB = new PrimitiveSqlDataType<>(Blob.class);
    SqlDataType<Byte> TINYINT = new PrimitiveSqlDataType<>(Byte.class);
    SqlDataType<Boolean> BOOLEAN = new PrimitiveSqlDataType<>(Boolean.class);
    SqlDataType<Boolean> BOOL = BOOLEAN;
    SqlDataType<Date> DATE = new PrimitiveSqlDataType<>(Date.class);
    SqlDataType<Timestamp> DATETIME = new PrimitiveSqlDataType<>(Timestamp.class);
    SqlDataType<Timestamp> TIMESTAMP = new PrimitiveSqlDataType<>(Timestamp.class);

    SqlDataType<LocalDateTime> LOCAL_DATE_TIME = new PrimitiveSqlDataType<>(LocalDateTime.class)
    {
        @Override
        public String getName()
        {
            return "TIMESTAMP";
        }
    };
    SqlDataType<Time> TIME = new PrimitiveSqlDataType<>(Time.class);
    SqlDataType<Integer> YEAR = new PrimitiveSqlDataType<>(Integer.class);
    SqlDataType<UUID> UNIQUE_IDENTIFIER = new PrimitiveSqlDataType<>(UUID.class);
    SqlDataType<String> XML = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<ResultSet> CURSOR = new PrimitiveSqlDataType<>(ResultSet.class);

    SqlDataType<String> CHAR = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<Short> SMALLINT = new PrimitiveSqlDataType<>(Short.class);
    SqlDataType<Integer> MEDIUMINT = new PrimitiveSqlDataType<>(Integer.class);
    SqlDataType<Integer> INTEGER = new PrimitiveSqlDataType<>(Integer.class);
    SqlDataType<Integer> INT = INTEGER;
    SqlDataType<Float> FLOAT = new PrimitiveSqlDataType<>(Float.class);
    SqlDataType<Double> DOUBLE = new PrimitiveSqlDataType<>(Double.class);
    SqlDataType<BigDecimal> DECIMAL = new PrimitiveSqlDataType<>(BigDecimal.class);
    SqlDataType<BigDecimal> DEC = DECIMAL;

    SqlDataType<BigDecimal> NUMERIC = new PrimitiveSqlDataType<>(BigDecimal.class);
    SqlDataType<Long> BIGINT = new PrimitiveSqlDataType<>(Long.class);
    SqlDataType<String> VARCHAR = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<String> TEXT = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<String> JSONB = new PrimitiveSqlDataType<>(String.class);

    static SqlDataType<String> CHAR(int length)
    {
        return new VariableSqlDataType<>(String.class, length);
    }

    static SqlDataType<Short> SMALLINT(int length)
    {
        return new VariableSqlDataType<>(Short.class, length);
    }

    static SqlDataType<Integer> MEDIUMINT(int length)
    {
        return new VariableSqlDataType<>(Integer.class, length);
    }

    static SqlDataType<Integer> INTEGER(int length)
    {
        return new VariableSqlDataType<>(Integer.class, length);
    }

    static SqlDataType<Integer> INT(int length)
    {
        return INTEGER(length);
    }

    static SqlDataType<Float> FLOAT(int length)
    {
        return new VariableSqlDataType<>(Float.class, length);
    }

    static SqlDataType<Double> DOUBLE(int length)
    {
        return new VariableSqlDataType<>(Double.class, length);
    }

    static SqlDataType<BigDecimal> DECIMAL(int length)
    {
        return new VariableSqlDataType<>(BigDecimal.class, length);
    }

    static SqlDataType<BigDecimal> DEC(int length)
    {
        return DECIMAL(length);
    }

    static SqlDataType<BigDecimal> NUMERIC(int precision, int scale)
    {
        return new NumericSqlDataType<>(precision, scale);
    }

    static SqlDataType<Long> BIGINT(int length)
    {
        return new VariableSqlDataType<>(Long.class, length);
    }

    static SqlDataType<String> VARCHAR(int length)
    {
        return new VariableSqlDataType<>(String.class, length);
    }

    static SqlDataType<String> TEXT(int length)
    {
        return new VariableSqlDataType<>(String.class, length);
    }

    static SqlDataType<byte[]> BINARY(int length)
    {
        return new VariableSqlDataType<>(byte[].class, length);
    }

    static SqlDataType<byte[]> VARBINARY(int length)
    {
        return new VariableSqlDataType<>(byte[].class, length);
    }

    static SqlDataType<Boolean> BIT(int length)
    {
        return new VariableSqlDataType<>(Boolean.class, length);
    }

    static SqlDataType<Blob> BLOB(int length)
    {
        return new VariableSqlDataType<>(Blob.class, length);
    }

    static SqlDataType<String> ENUM(Object... values)
    {
        return new ValuedSqlDataType<>(String.class, values);
    }

    static SqlDataType<Set> SET(Object... values)
    {
        return new ValuedSqlDataType<>(Set.class, values);
    }

    static SqlDataType<?> ARRAY(SqlDataType<?> inner)
    {
        return new ArraySqlDataType<>(inner);
    }

    /**
     * Resolves a {@code SqlDataType} by matching its static field name to a specified string.
     *
     * @param string the name of the data type to lookup
     * @param <T>    the inferred internal Java type mapping
     * @return the matching SqlDataType instance, or {@code null} if not found
     */
    static <T> SqlDataType<T> fromString(String string)
    {
        return Arrays.stream(SqlDataType.class.getDeclaredFields()).filter(field -> field.getName().equals(string))
                .map(field ->
                {
                    try
                    {
                        @SuppressWarnings("unchecked")
                        SqlDataType<T> dataType = (SqlDataType<T>) field.get(null);
                        return dataType;
                    }
                    catch(IllegalAccessException e)
                    {
                        return null;
                    }
                }).findAny().orElse(null);
    }

    /**
     * Resolves the SqlDataType from a Java Field, processing {@link Column} definitions first. Fallback to reflection on raw types and components is used if
     * explicit DDL definitions are missing.
     *
     * @param field the Java field reflecting a database column
     * @return the resolved SQL data type mapping
     */
    static SqlDataType<?> fromField(Field field)
    {
        Column column = field.getAnnotation(Column.class);

        if(column != null && column.columnDefinition().trim().equalsIgnoreCase("JSONB"))
            return JSONB;

        if(column != null && !column.columnDefinition().isEmpty())
        {
            String definition = column.columnDefinition().trim().toUpperCase();

            Pattern complexPattern = Pattern.compile("^([A-Z]+)\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)");
            Matcher complexMatcher = complexPattern.matcher(definition);
            if(complexMatcher.find())
            {
                String typeName = complexMatcher.group(1);
                int p1 = Integer.parseInt(complexMatcher.group(2));
                int p2 = Integer.parseInt(complexMatcher.group(3));

                if(typeName.equals("NUMERIC"))
                    return NUMERIC(p1, p2);
                if(typeName.equals("DECIMAL") || typeName.equals("DEC"))
                    return DECIMAL(p1);
            }

            Pattern simplePattern = Pattern.compile("^([A-Z]+)\\s*\\(\\s*(\\d+)\\s*\\)");
            Matcher simpleMatcher = simplePattern.matcher(definition);
            if(simpleMatcher.find())
            {
                String typeName = simpleMatcher.group(1);
                int length = Integer.parseInt(simpleMatcher.group(2));

                switch(typeName)
                {
                    case "VARCHAR" -> VARCHAR(length);
                    case "TEXT" -> TEXT(length);
                    case "INT", "INTEGER" -> INT(length);
                    case "BIGINT" -> BIGINT(length);
                    case "DOUBLE" -> DOUBLE(length);
                    case "FLOAT" -> FLOAT(length);
                    case "CHAR" -> CHAR(length);
                }
            }

            SqlDataType<?> parsed = fromString(definition);
            if(parsed != null)
                return parsed;
        }

        Class<?> type = field.getType();

        if(List.class.isAssignableFrom(type))
        {
            Type genericType = field.getGenericType();
            if(genericType instanceof ParameterizedType parameterizedType)
            {
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                if(typeArguments.length > 0 && typeArguments[0] instanceof Class<?> listClass)
                    return ARRAY(fromJavaType(listClass));
            }
            return ARRAY(TEXT);
        }

        return fromJavaType(type);
    }

    private static SqlDataType<?> fromJavaType(Class<?> type)
    {
        if(type == int.class || type == Integer.class)
            return INT;
        if(type == long.class || type == Long.class)
            return BIGINT;
        if(type == String.class)
            return VARCHAR(255);
        if(type == boolean.class || type == Boolean.class)
            return BOOLEAN;
        if(type == double.class || type == Double.class)
            return DOUBLE;
        if(type == float.class || type == Float.class)
            return FLOAT;
        if(type == BigDecimal.class)
            return NUMERIC;
        if(type == Date.class)
            return DATE;
        if(type == Timestamp.class)
            return TIMESTAMP;
        if(type == LocalDateTime.class)
            return LOCAL_DATE_TIME;
        if(type == UUID.class)
            return UNIQUE_IDENTIFIER;

        if(type.isEnum())
            return VARCHAR(255);

        if(type.isArray())
        {
            Class<?> component = type.getComponentType();
            return ARRAY(fromJavaType(component));
        }
        if(List.class.isAssignableFrom(type))
            return ARRAY(INT);

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    /**
     * Gets the standard SQL string representation of this data type.
     *
     * @return the SQL data type string
     */
    String getName();

    /**
     * Helper to wrap this data type context alongside a specific column name.
     *
     * @param name the target table column name
     * @return a structured TableColumn instance containing this type data
     */
    default TableColumn withName(String name)
    {
        return new TableColumn(name, this);
    }

    /**
     * Internal implementation mapping straightforward primitive or structural types.
     */
    class PrimitiveSqlDataType<T> implements SqlDataType<T>
    {
        protected final Class<T> dataType;
        protected String methodName;

        private PrimitiveSqlDataType(Class<T> dataType)
        {
            this.dataType = dataType;

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if(stackTraceElements.length > 4)
            {
                if(Arrays.stream(SqlDataType.class.getDeclaredMethods())
                        .anyMatch(method -> method.getName().equals(stackTraceElements[3].getMethodName())))
                    methodName = stackTraceElements[3].getMethodName();
            }
        }

        /**
         * Returns the internal Java type class bound to this SQL structure.
         *
         * @return the java type representation class
         */
        public Class<T> getDataType()
        {
            return dataType;
        }

        @Override
        public String getName()
        {
            if(methodName != null)
                return methodName;

            for(Field field : SqlDataType.class.getDeclaredFields())
            {
                try
                {
                    if(field.get(null) == this)
                        return field.getName().replace("_", "");
                }
                catch(IllegalAccessException e)
                {
                }
            }
            return null;
        }
    }

    /**
     * Structural extension tracking sizing or length variables (e.g. VARCHAR(length)).
     */
    class VariableSqlDataType<T> extends PrimitiveSqlDataType<T>
    {
        private final int length;

        private VariableSqlDataType(Class<T> dataType, int length)
        {
            super(dataType);
            this.length = length;
        }

        @Override
        public String getName()
        {
            return super.getName() + "(" + length + ")";
        }
    }

    /**
     * Specific structural type for handling custom high-precision numerical scales (e.g. NUMERIC(10,2)).
     */
    class NumericSqlDataType<T> extends PrimitiveSqlDataType<T>
    {
        private final int precision;
        private final int scale;

        @SuppressWarnings("unchecked")
        private NumericSqlDataType(int precision, int scale)
        {
            super((Class<T>) BigDecimal.class);
            this.precision = precision;
            this.scale = scale;
        }

        @Override
        public String getName()
        {
            return "NUMERIC(" + precision + ", " + scale + ")";
        }
    }

    /**
     * Type definition formatting a predefined bounded value restriction list (e.g. ENUM or SET).
     */
    class ValuedSqlDataType<T> extends PrimitiveSqlDataType<T>
    {
        private final Object[] objects;

        private ValuedSqlDataType(Class<T> dataType, Object... objects)
        {
            super(dataType);
            this.objects = objects;
        }

        @Override
        public String getName()
        {
            return super.getName() + "(" + Arrays.stream(objects).map(obj -> "'" + obj.toString() + "'")
                    .collect(Collectors.joining(", ")) + ")";
        }
    }

    /**
     * Custom container mapping an underlying data type as a database array representation.
     */
    class ArraySqlDataType<T> implements SqlDataType<T>
    {
        private final SqlDataType<?> inner;

        public ArraySqlDataType(SqlDataType<?> inner)
        {
            this.inner = inner;
        }

        @Override
        public String getName()
        {
            return (inner != null ? inner.getName() : "TEXT") + "[]";
        }
    }
}