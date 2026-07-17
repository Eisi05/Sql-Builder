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

/**
 * Represents a SQL CREATE TABLE statement used to create structural tables inside databases.
 */
public class CreateTableStatement extends FinalStatement
        implements CreateTableAsStatement.CreateTableAsStatementContainer, ExecuteUpdateStatement
{
    private boolean ifNotExists = false;

    /**
     * Constructs a CreateTableStatement targeting the given table.
     *
     * @param table the table identifier or full declaration block
     */
    protected CreateTableStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword representing this statement, optionally adding "IF NOT EXISTS" constraints.
     *
     * @return "CREATE TABLE" or "CREATE TABLE IF NOT EXISTS"
     */
    @Override
    protected String getKey()
    {
        return "CREATE TABLE" + (ifNotExists ? " IF NOT EXISTS" : "");
    }

    /**
     * Interface for containers that can build CREATE TABLE structures manually or via ORM mappings.
     */
    public interface CreateTableStatementContainer extends StatementContainer
    {
        /**
         * Creates a custom table using manual TableColumn definitions.
         *
         * @param table   the target table identifier
         * @param column  the first required column
         * @param columns subsequent columns to append
         * @return a new CreateTableStatement
         * @throws PrimaryKeyException if more than one primary key is detected in the columns
         */
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

        /**
         * Creates a skeletal CREATE TABLE query.
         *
         * @param table the table name
         * @return a new CreateTableStatement
         */
        default CreateTableStatement createTable(String table)
        {
            return create(new CreateTableStatement(table));
        }

        /**
         * Creates a CREATE TABLE statement dynamically mapped from ORM entity reflections.
         *
         * @param clazz the entity class containing field annotations
         * @return a completed CreateTableStatement mapping class fields to table columns
         * @throws PrimaryKeyException if multiple primary keys are resolved in reflections
         */
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

        /**
         * Creates a CREATE TABLE IF NOT EXISTS statement mapped from an entity's reflections.
         *
         * @param clazz the class mapped with database annotations
         * @return a CreateTableStatement targeting safe creation sequences
         */
        default CreateTableStatement createTableIfNotExists(Class<?> clazz)
        {
            CreateTableStatement statement = createTable(clazz);
            statement.ifNotExists = true;
            return statement;
        }

        /**
         * Creates a CREATE TABLE IF NOT EXISTS statement using manual TableColumn configurations.
         *
         * @param table   the target table identifier
         * @param column  the primary required column
         * @param columns any subsequent columns
         * @return a CreateTableStatement targeting safe creation sequences
         */
        default CreateTableStatement createTableIfNotExists(String table, TableColumn column, TableColumn... columns)
        {
            CreateTableStatement statement = createTable(table, column, columns);
            statement.ifNotExists = true;
            return statement;
        }
    }
}