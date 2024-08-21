package de.eisi05.sql.interfaces;

import de.eisi05.sql.statements.table.TableColumn;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface SqlDataType<T>
{
    int DEFAULT_CHAR_LENGTH = 255;
    int DEFAULT_VARCHAR_LENGTH = 255;
    int DEFAULT_TEXT_LENGTH = 65535;
    int DEFAULT_NUMERIC_LENGTH = 10;

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

    static <T> SqlDataType<T> fromString(String string)
    {
        return Arrays.stream(SqlDataType.class.getDeclaredFields()).filter(field -> field.getName().equals(string))
                .map(field ->
                {
                    try
                    {
                        return (SqlDataType<T>) field.get(null);
                    } catch(IllegalAccessException e)
                    {
                        return null;
                    }
                }).findAny().orElse(null);
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
                } catch(IllegalAccessException e)
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
}
