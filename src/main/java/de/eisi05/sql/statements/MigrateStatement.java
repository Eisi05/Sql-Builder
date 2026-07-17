package de.eisi05.sql.statements;

import de.eisi05.sql.annotations.Column;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.utils.OrmUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a database migration statement. Automatically generates ALTER TABLE statements to synchronize the database schema with the entity class definition
 * using ORM annotations.
 */
public class MigrateStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new MigrateStatement.
     */
    protected MigrateStatement()
    {
        super(null);
    }

    /**
     * Gets the SQL keyword for this statement. Returns empty string since migration statements don't have a keyword.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create MIGRATE statements.
     */
    public interface MigrateStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a migration statement for the specified entity class. Generates ALTER TABLE statements to add missing columns and remove obsolete ones.
         *
         * @param clazz the entity class to migrate
         * @return a new MigrateStatement with the migration chain
         */
        default FinalStatement migrate(Class<?> clazz)
        {
            try
            {
                return buildMigrationChain(clazz);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                return create(new MigrateStatement());
            }
        }

        /**
         * Builds the migration chain by comparing entity and database schemas.
         *
         * @param clazz the entity class
         * @return a MigrateStatement with the migration chain
         * @throws SQLException if database metadata access fails
         */
        private FinalStatement buildMigrationChain(Class<?> clazz) throws SQLException
        {
            MigrateStatement migrateStatement = create(new MigrateStatement());
            DatabaseStatement statement = migrateStatement.getDatabaseStatement();
            if(statement == null)
                return migrateStatement;

            Connection connection = statement.getConnection();
            String tableName = OrmUtils.resolveTable(clazz);

            Map<String, FieldColumnMeta> entityColumns = getEntityColumns(clazz);
            Map<String, DbColumnMeta> databaseColumns = getDatabaseColumns(connection, tableName);

            if(databaseColumns.isEmpty())
                return statement.createTableIfNotExists(clazz);

            for(Map.Entry<String, FieldColumnMeta> entry : entityColumns.entrySet())
            {
                String currentName = entry.getKey();
                FieldColumnMeta expected = entry.getValue();

                if(expected.oldName != null && !expected.oldName.isEmpty())
                {
                    String oldName = expected.oldName;

                    if(databaseColumns.containsKey(oldName) && !databaseColumns.containsKey(currentName))
                    {
                        migrateStatement.appendStatement(statement.alterTable(tableName).renameColumn(oldName, currentName));

                        DbColumnMeta meta = databaseColumns.remove(oldName);
                        databaseColumns.put(currentName, meta);
                    }
                }
            }

            for(String dbColName : databaseColumns.keySet())
            {
                if(!entityColumns.containsKey(dbColName))
                    migrateStatement.appendStatement(statement.alterTable(tableName).dropColumn(dbColName));
            }

            return migrateStatement;
        }

        /**
         * Extracts column metadata from the entity class fields.
         *
         * @param clazz the entity class
         * @return a map of column names to their metadata
         */
        private Map<String, FieldColumnMeta> getEntityColumns(Class<?> clazz)
        {
            Map<String, FieldColumnMeta> columns = new LinkedHashMap<>();
            for(Field field : clazz.getDeclaredFields())
            {
                Column column = field.getAnnotation(Column.class);
                String name = (column != null && !column.name().isEmpty()) ? column.name() : field.getName();
                String oldName = column != null ? column.oldName() : "";

                boolean isNotNull = column != null && column.notNull();
                boolean isUnique = column != null && column.unique();
                String defaultValue = column != null ? column.defaultValue() : null;
                SqlDataType<?> sqlType = SqlDataType.fromField(field);

                columns.put(name, new FieldColumnMeta(sqlType, isNotNull, isUnique, defaultValue, oldName));
            }
            return columns;
        }

        /**
         * Retrieves existing column names from the database.
         *
         * @param conn      the database connection
         * @param tableName the table name
         * @return a map of column names to their metadata
         * @throws SQLException if database metadata access fails
         */
        private Map<String, DbColumnMeta> getDatabaseColumns(Connection conn, String tableName) throws SQLException
        {
            Map<String, DbColumnMeta> columns = new LinkedHashMap<>();
            DatabaseMetaData metaData = conn.getMetaData();
            try(ResultSet rs = metaData.getColumns(null, null, tableName, null))
            {
                while(rs.next())
                {
                    String columnName = rs.getString("COLUMN_NAME");
                    columns.put(columnName, new DbColumnMeta());
                }
            }
            return columns;
        }
    }

    /**
     * Record representing column metadata from an entity field.
     *
     * @param sqlType      the SQL data type
     * @param isNotNull    whether the column is NOT NULL
     * @param isUnique     whether the column is UNIQUE
     * @param defaultValue the default value for the column
     * @param oldName      the previous name of the column, used to trace schema rename operations
     */
    private record FieldColumnMeta(SqlDataType<?> sqlType, boolean isNotNull, boolean isUnique, String defaultValue, String oldName) {}

    /**
     * Record representing column metadata from the database.
     */
    private record DbColumnMeta() {}
}