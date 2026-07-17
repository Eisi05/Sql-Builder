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
 * Represents a database migration statement. Automatically generates <b>{@code ALTER TABLE}</b> or <b>{@code CREATE TABLE}</b> statements to synchronize the
 * live database schema with the entity class definitions using ORM annotations.
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
     * Gets the SQL keyword for this statement. Returns an empty string since migration statements act as a wrapper chain container and don't have a standalone
     * root keyword.
     *
     * @return empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }

    /**
     * Interface for containers that can create <b>{@code MIGRATE}</b> statements.
     */
    public interface MigrateStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Creates a migration statement chain for the specified entity class. Automatically determines whether to bootstrap a brand-new table or structurally
         * modify an existing one by appending rename, drop, or add operations.
         *
         * @param clazz the entity class to migrate
         * @return a FinalStatement containing the generated schema synchronization operations
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
         * Builds the migration chain by comparing the entity class schema definition against the live database metadata.
         * <p>
         * If the target table does not exist in the database (or contains no columns), this method routes directly to creating the table. Otherwise, it
         * iteratively builds a structural alteration chain by matching, renaming, or deleting target database columns.
         *
         * @param clazz the entity class
         * @return a FinalStatement containing either a table creation statement or a series of structural table alterations
         * @throws SQLException if database metadata access or connection retrieval fails
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
         * Extracts column metadata from the entity class fields by reading local structural definitions and {@link Column} annotations.
         *
         * @param clazz the entity class
         * @return a map tracking target column names mapped to their expected object field metadata configuration
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
         * Retrieves existing column structures from the target database table using JDBC metadata mappings.
         *
         * @param conn      the active database connection instance
         * @param tableName the database table name to inspect
         * @return a map tracking active column names mapped to their live structural footprint profiles
         * @throws SQLException if structural information retrieval or database metadata reading fails
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
     * Record representing column metadata mapped out of an explicit entity field configuration.
     *
     * @param sqlType      the physical SQL data type translation rule
     * @param isNotNull    whether the target column specifies a <b>{@code NOT NULL}</b> constraint
     * @param isUnique     whether the target column specifies a <b>{@code UNIQUE}</b> constraint
     * @param defaultValue the fallback literal default string evaluation expression
     * @param oldName      the historical name configuration tracking prior renames
     */
    private record FieldColumnMeta(SqlDataType<?> sqlType, boolean isNotNull, boolean isUnique, String defaultValue, String oldName) {}

    /**
     * Record representing existence metadata for a column found directly inside the live database.
     */
    private record DbColumnMeta() {}
}