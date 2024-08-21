package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.statements.where.AbstractWhereStatement;
import de.eisi05.sql.statements.where.WhereStatement;

import java.util.function.Function;

public class TableColumn
{
    private final String name;
    private final SqlDataType<?> dataType;

    private boolean isNotNull = false;
    private boolean isUnique = false;
    private boolean isPrimaryKey = false;
    private String check = null;
    private String defaultValue = null;
    private int[] autoIncrement = null;

    public TableColumn(String name, SqlDataType<?> dataType)
    {
        this.name = name;
        this.dataType = dataType;
    }

    public TableColumn notNull()
    {
        isNotNull = true;
        return this;
    }

    public TableColumn unique()
    {
        isUnique = true;
        return this;
    }

    public TableColumn primaryKey()
    {
        isPrimaryKey = true;
        return this;
    }

    public TableColumn defaultValue(String defaultValue)
    {
        this.defaultValue = "DEFAULT (" + defaultValue + ")";
        return this;
    }

    public TableColumn setDefaultValue(String defaultValue)
    {
        this.defaultValue = "SET DEFAULT (" + defaultValue + ")";
        return this;
    }

    public TableColumn autoIncrement()
    {
        this.autoIncrement = new int[1];
        return this;
    }

    public TableColumn autoIncrement(int start, int increment)
    {
        this.autoIncrement = new int[]{start, increment};
        return this;
    }

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

    boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }

    public String getName()
    {
        return name;
    }

    public SqlDataType<?> getDataType()
    {
        return dataType;
    }

    public String asQuery()
    {
        return name + " " + dataType.getName() +
                (autoIncrement != null && autoIncrement.length == 2 ?
                        " IDENTITY(" + autoIncrement[0] + "," + autoIncrement[1] + ")" : "") +
                (isNotNull ? " NOT NULL" : "") + (isUnique ? " UNIQUE" : "") +
                (isPrimaryKey ? " PRIMARY KEY" : "") + (check != null ? " CHECK (" + check + ")" : "") +
                (defaultValue != null ? defaultValue : "") +
                (autoIncrement != null && autoIncrement.length < 2 ? " AUTO_INCREMENT" : "");
    }
}
