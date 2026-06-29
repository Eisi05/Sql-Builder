package de.eisi05.sql.statements.table;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.annotations.GeneratedValue;
import de.eisi05.sql.annotations.Id;
import de.eisi05.sql.database.PostgresDatabase;
import de.eisi05.sql.exceptions.PrimaryKeyException;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.statements.FinalStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateTableStatement extends FinalStatement
        implements CreateTableAsStatement.CreateTableAsStatementContainer, ExecuteUpdateStatement
{
    private boolean ifNotExists = false;

    protected CreateTableStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "CREATE TABLE" + (ifNotExists ? " IF NOT EXISTS" : "");
    }

    public interface CreateTableStatementContainer extends StatementContainer
    {
        default CreateTableStatement createTable(String table, TableColumn column, TableColumn... columns)
        {
            columns = Stream.concat(Arrays.stream(columns), Stream.of(column)).toArray(TableColumn[]::new);

            if(Arrays.stream(columns).filter(TableColumn::isPrimaryKey).count() > 1)
                throw new PrimaryKeyException("Cannot have more than one primary key in a table");

            return create(new CreateTableStatement(
                    table + " (" + Arrays.stream(columns).map(tableColumn -> tableColumn.asQuery(getDatabase()
                                    .map(database -> database instanceof PostgresDatabase).orElse(false)))
                            .collect(Collectors.joining(", ")) + ")"));
        }

        default CreateTableStatement createTable(String table)
        {
            return create(new CreateTableStatement(table));
        }

        default CreateTableStatement createTable(Class<?> clazz)
        {
            String tableName = OrmUtils.resolveTable(clazz);

            TableColumn[] columns = Arrays.stream(clazz.getDeclaredFields())
                    .map(field ->
                    {
                        Column column = field.getAnnotation(Column.class);

                        String name = (column != null && !column.name().isEmpty()) ? column.name() : field.getName();

                        boolean isPrimaryKey = field.isAnnotationPresent(Id.class);
                        boolean isGenerated = field.isAnnotationPresent(GeneratedValue.class);
                        boolean isNotNull = column != null && column.notNull();
                        boolean isUnique = column != null && column.unique();
                        String defaultValue = column != null ? column.defaultValue() : null;

                        SqlDataType<?> sqlType = SqlDataType.fromField(field);
                        TableColumn col = new TableColumn(name, sqlType);

                        if(isPrimaryKey)
                            col.primaryKey();
                        if(isGenerated)
                            col.autoIncrement();
                        if(isNotNull)
                            col.notNull();
                        if(isUnique)
                            col.unique();
                        if(defaultValue != null && !defaultValue.isEmpty())
                            col.defaultValue(defaultValue);

                        return col;
                    })
                    .toArray(TableColumn[]::new);

            if(Arrays.stream(columns).filter(TableColumn::isPrimaryKey).count() > 1)
                throw new PrimaryKeyException("Cannot have more than one primary key in a table");

            return createTable(tableName, columns[0], Arrays.copyOfRange(columns, 1, columns.length));
        }

        default CreateTableStatement createTableIfNotExists(Class<?> clazz)
        {
            CreateTableStatement statement = createTable(clazz);
            statement.ifNotExists = true;
            return statement;
        }

        default CreateTableStatement createTableIfNotExists(String table, TableColumn column, TableColumn... columns)
        {
            CreateTableStatement statement = createTable(table, column, columns);
            statement.ifNotExists = true;
            return statement;
        }
    }
}
