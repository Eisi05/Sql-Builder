package de.eisi05.sql.interfaces;

import de.eisi05.sql.statements.table.TableColumn;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public interface SqlDataType<T>
{
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
    SqlDataType<Long> BIGINT = new PrimitiveSqlDataType<>(Long.class);
    SqlDataType<String> VARCHAR = new PrimitiveSqlDataType<>(String.class);
    SqlDataType<String> TEXT = new PrimitiveSqlDataType<>(String.class);

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

    static SqlDataType<?> fromField(Field field)
    {
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

    static SqlDataType<?> fromJavaType(Class<?> type)
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
        if(type == Date.class)
            return DATE;
        if(type == Timestamp.class)
            return TIMESTAMP;
        if(type == UUID.class)
            return UNIQUE_IDENTIFIER;
        if(type.isArray())
        {
            Class<?> component = type.getComponentType();
            return ARRAY(fromJavaType(component));
        }
        if(List.class.isAssignableFrom(type))
            return ARRAY(INT);

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    String getName();

    default TableColumn withName(String name)
    {
        return new TableColumn(name, this);
    }

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
            return super.getName() + "(" +
                    Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(", ")) + ")";
        }
    }

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
