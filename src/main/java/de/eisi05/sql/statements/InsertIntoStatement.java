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
            String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));
            InsertIntoStatement statement = create(new InsertIntoStatement(table + " VALUES (" + placeholders + ")"));
            statement.localParameters.addAll(Arrays.asList(values));
            return statement;
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
            String valuesPlaceholders = rows.stream()
                    .map(row -> "(" + String.join(", ", Collections.nCopies(row.size(), "?")) + ")")
                    .collect(Collectors.joining(", "));

            return create(new InsertIntoStatement(table + " (" + columns + ") VALUES " + valuesPlaceholders),
                    rows.stream().flatMap(stringObjectMap -> stringObjectMap.values().stream()).toList());
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
            List<String> rowsPlaceholders = new ArrayList<>();
            List<Object> rawValuesCollector = new ArrayList<>();

            for(int i = 0; i < numRows; i++)
            {
                List<String> rowPlaceholders = new ArrayList<>();
                for(String column : columns)
                {
                    List<Object> values = columnValuesMap.get(column);
                    if(values != null && i < values.size())
                    {
                        rowPlaceholders.add("?");
                        rawValuesCollector.add(values.get(i));
                    }
                }
                rowsPlaceholders.add("(" + String.join(", ", rowPlaceholders) + ")");
            }

            return create(new InsertIntoStatement(table + " (" + String.join(", ", columns) + ")" +
                    " VALUES " + String.join(", ", rowsPlaceholders)), rawValuesCollector);
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
