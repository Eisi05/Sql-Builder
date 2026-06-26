package de.eisi05.sql.statements;

import de.eisi05.sql.database.Database;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;

public abstract class DatabaseStatement extends AbstractStatement
        implements AbstractStatement.DefaultStatementContainers
{
    protected final Database database;
    protected final Connection connection;

    protected DatabaseStatement(Database database, Connection connection)
    {
        super(null);
        this.database = database;
        this.connection = connection;
    }

    public static DatabaseStatement fromJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        return new DatabaseStatement(null, DataSourceUtils.getConnection(jdbcTemplate.getDataSource())){};
    }

    public Connection getConnection()
    {
        return connection;
    }

    @Override
    protected String getKey()
    {
        return "";
    }
}
