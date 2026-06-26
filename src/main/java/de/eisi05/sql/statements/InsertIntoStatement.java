package de.eisi05.sql.statements;

import de.eisi05.sql.exceptions.InsertionException;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.select.SelectStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.*;
import java.util.stream.Collectors;

public class InsertIntoStatement extends FinalStatement implements ExecuteUpdateStatement
{
    private InsertIntoStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "INSERT INTO";
    }

    public interface InsertIntoStatementContainer extends StatementContainer
    {
        default InsertIntoStatement insertInto(String table, Object... values)
        {
            return create(new InsertIntoStatement(table + " VALUES (" + Arrays.stream(values).map(o ->
            {
                if(o instanceof String)
                    return "'" + o + "'";
                return o.toString();
            }).collect(Collectors.joining(", ")) + ")"));
        }

        default InsertIntoSelectStatement insertInto(String table, String... keys)
        {
            return create(new InsertIntoSelectStatement(table + " (" + String.join(", ", keys) + ")"));
        }

        default <T> InsertIntoStatement insertInto(T... objects)
        {
            if(objects.length == 0)
                throw new IllegalArgumentException("No objects provided");

            String table = OrmUtils.resolveTable(objects[0].getClass());

            List<Map<String, Object>> rows = Arrays.stream(objects)
                    .map(OrmUtils::toColumnMap)
                    .toList();

            String columns = String.join(", ", rows.getFirst().keySet());

            String values = rows.stream()
                    .map(row -> "(" +
                            row.values().stream()
                                    .map(OrmUtils::formatValue)
                                    .collect(Collectors.joining(", ")) +
                            ")")
                    .collect(Collectors.joining(", "));

            return create(new InsertIntoStatement(table + " (" + columns + ") VALUES " + values));
        }

        default InsertIntoStatement insertInto(String table, InsertObject... insertObjects)
        {
            Optional<InsertObject> nonMatching = Arrays.stream(insertObjects)
                    .collect(Collectors.groupingBy(obj -> obj.values().length))
                    .values()
                    .stream()
                    .filter(list -> list.size() == 1)
                    .map(List::getFirst)
                    .findFirst();

            if(nonMatching.isPresent() && insertObjects.length > 1)
                throw new InsertionException(nonMatching.get().key + " does not match the required length of " +
                        Arrays.stream(insertObjects).max(Comparator.comparingInt(o -> o.values().length))
                                .get().values.length);

            List<String> columns = Arrays.stream(insertObjects)
                    .map(InsertObject::key)
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, List<Object>> columnValuesMap = new LinkedHashMap<>();
            for(String column : columns)
                columnValuesMap.put(column, new ArrayList<>());

            for(InsertObject obj : insertObjects)
            {
                List<Object> valuesList = columnValuesMap.get(obj.key());
                if(valuesList != null)
                    valuesList.addAll(Arrays.asList(obj.values()));
            }

            int numRows = columnValuesMap.values().stream().mapToInt(List::size).max().orElse(0);
            List<String> rows = new ArrayList<>();
            for(int i = 0; i < numRows; i++)
            {
                List<String> rowValues = new ArrayList<>();
                for(String column : columns)
                {
                    List<Object> values = columnValuesMap.get(column);
                    if(values != null && i < values.size())
                        rowValues.add(values.get(i) instanceof String ? "'" + values.get(i) + "'" : values.get(i) + "");
                }
                rows.add("(" + String.join(", ", rowValues) + ")");
            }

            return create(new InsertIntoStatement(table + " (" + String.join(", ", columns) + ")" +
                    " VALUES " + String.join(", ", rows)));
        }

        default InsertIntoStatement insertInto(String table, Map<String, Object[]> insertMap)
        {
            return insertInto(table, insertMap.entrySet().stream()
                    .map(stringEntry -> new InsertObject(stringEntry.getKey(), stringEntry.getValue())).toList());
        }
    }

    public record InsertObject(String key, Object... values)
    {
    }

    public static class InsertIntoSelectStatement extends InsertIntoStatement
            implements SelectStatement.SelectStatementContainer
    {
        private InsertIntoSelectStatement(String query)
        {
            super(query);
        }
    }
}
