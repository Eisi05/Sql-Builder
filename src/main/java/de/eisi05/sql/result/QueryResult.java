package de.eisi05.sql.result;

import de.eisi05.sql.interfaces.SqlDataType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class QueryResult
{
    private final LinkedHashMap<String, Object> results;

    public QueryResult(ResultSet resultSet) throws SQLException
    {
        this.results = new LinkedHashMap<>();

        for(int i = 1; resultSet.next(); i++)
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
}
