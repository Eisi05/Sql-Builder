package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.statements.where.AbstractWhereStatement;
import de.eisi05.sql.statements.where.WhereStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Builder class and model representation for building out structural SQL Table Columns and their constraints.
 */
public class TableColumn
{
    private final String name;
    private final SqlDataType<?> dataType;
    private final List<String> constraints = new ArrayList<>();
    private boolean isNotNull = false;
    private boolean isUnique = false;
    private boolean isPrimaryKey = false;
    private String check = null;
    private String defaultValue = null;
    private int[] autoIncrement = null;

    /**
     * Constructs a new TableColumn with standard property values.
     *
     * @param name     the label descriptor for the column
     * @param dataType the mapping SQL structural type
     */
    public TableColumn(String name, SqlDataType<?> dataType)
    {
        this.name = name;
        this.dataType = dataType;
    }

    /**
     * Fluent interface helper to instantiate a TableColumn inline.
     *
     * @param name     the column label descriptor
     * @param dataType the target SqlDataType entity
     * @return a new TableColumn instance
     */
    public static TableColumn of(String name, SqlDataType<?> dataType)
    {
        return new TableColumn(name, dataType);
    }

    /**
     * Flags this column instance to incorporate a <b>{@code NOT NULL}</b> constraint.
     *
     * @return the builder instance for chaining
     */
    public TableColumn notNull()
    {
        isNotNull = true;
        return this;
    }

    /**
     * Flags this column instance to enforce a <b>{@code UNIQUE}</b> value restriction.
     *
     * @return the builder instance for chaining
     */
    public TableColumn unique()
    {
        isUnique = true;
        return this;
    }

    /**
     * Sets this column instance as a <b>{@code PRIMARY KEY}</b> identifier.
     *
     * @return the builder instance for chaining
     */
    public TableColumn primaryKey()
    {
        isPrimaryKey = true;
        return this;
    }

    /**
     * Sets a traditional inline SQL <b>{@code DEFAULT}</b> value modifier.
     *
     * @param defaultValue the raw default value expression string
     * @return the builder instance for chaining
     */
    public TableColumn defaultValue(String defaultValue)
    {
        this.defaultValue = "DEFAULT (" + defaultValue + ")";
        return this;
    }

    /**
     * Sets a modification statement to update defaults during column updates (e.g. <b>{@code SET DEFAULT}</b> ).
     *
     * @param defaultValue the raw default target expression string
     * @return the builder instance for chaining
     */
    public TableColumn setDefaultValue(String defaultValue)
    {
        this.defaultValue = "SET DEFAULT (" + defaultValue + ")";
        return this;
    }

    /**
     * Activates a standard database <b>{@code AUTO INCREMENT}</b> rule sequence on this column.
     *
     * @return the builder instance for chaining
     */
    public TableColumn autoIncrement()
    {
        this.autoIncrement = new int[1];
        return this;
    }

    /**
     * Activates an absolute structural identity mechanism defining custom start indices and increments.
     *
     * @param start     the base initial initialization identifier value
     * @param increment the offset increment mapping factor per added entry
     * @return the builder instance for chaining
     */
    public TableColumn autoIncrement(int start, int increment)
    {
        this.autoIncrement = new int[]{start, increment};
        return this;
    }

    /**
     * Appends an operational <b>{@code CHECK}</b> criteria filter routine evaluating inputs safely.
     *
     * @param key   the targeted verification base element context
     * @param check the functional mapping strategy converting conditions to where evaluation syntax blocks
     * @return the builder instance for chaining
     */
    public TableColumn check(String key, Function<WhereStatement, AbstractWhereStatement> check)
    {
        this.check = check.apply(new WhereStatement(key)
        {
            @Override
            public String getKey()
            {
                return (logicOperator != null ? logicOperator.name() : "") + (withNot ? " NOT" : "");
            }
        }).getQuery();
        return this;
    }

    /**
     * Links a relative structural <b>{@code FOREIGN KEY}</b> constraint validation map.
     *
     * @param table  the target table class reference to map relationships
     * @param column the target foreign column key descriptor
     * @return the builder instance for chaining
     */
    public TableColumn references(Class<?> table, String column)
    {
        constraints.add("REFERENCES " + OrmUtils.resolveTable(table) + "(" + column + ")");
        return this;
    }

    /**
     * Checks if this structural column represents a primary key target definition.
     *
     * @return true if primary key configurations are set, false otherwise
     */
    boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }

    /**
     * Returns the name label assigned to this column instance.
     *
     * @return the absolute textual column name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the type assigned to this column instance.
     *
     * @return the relative underlying database mapping type value descriptor
     */
    public SqlDataType<?> getDataType()
    {
        return dataType;
    }

    /**
     * Transforms builder properties into an absolute executable database-safe query fragment.
     *
     * @param isPostgres true if the query sequence targets Postgres database environments specifically
     * @return the formatted SQL column declaration string snippet
     */
    public String asQuery(boolean isPostgres)
    {
        StringBuilder sql = new StringBuilder();
        sql.append(name).append(" ").append(dataType.getName());

        if(autoIncrement != null && autoIncrement.length == 2)
            sql.append(" IDENTITY(").append(autoIncrement[0]).append(",").append(autoIncrement[1]).append(")");

        if(isNotNull)
            sql.append(" NOT NULL");
        if(isUnique)
            sql.append(" UNIQUE");

        boolean isPostgresIdentity = isPostgres && autoIncrement != null && autoIncrement.length < 2;
        if(defaultValue != null && !isPostgresIdentity)
            sql.append(" ").append(defaultValue.trim());

        if(autoIncrement != null && autoIncrement.length < 2)
        {
            if(isPostgres)
                sql.append(" GENERATED BY DEFAULT AS IDENTITY");
            else
                sql.append(" AUTO_INCREMENT");
        }

        if(isPrimaryKey)
            sql.append(" PRIMARY KEY");
        if(check != null)
            sql.append(" CHECK (").append(check).append(")");

        if(constraints != null && !constraints.isEmpty())
            sql.append(" ").append(String.join(" ", constraints));

        return sql.toString();
    }
}