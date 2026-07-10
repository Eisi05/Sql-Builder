package de.eisi05.sql.statements;

import de.eisi05.sql.database.Database;
import de.eisi05.sql.database.MySqlDatabase;
import de.eisi05.sql.database.PostgresDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.function.Supplier;

public abstract class DatabaseStatement extends AbstractStatement
        implements AbstractStatement.DefaultStatementContainers
{
    protected final Database database;
    private final Supplier<Connection> connectionSupplier;

    protected DatabaseStatement(Database database, Supplier<Connection> connectionSupplier)
    {
        super(null);
        this.database = database;
        this.connectionSupplier = connectionSupplier;
    }

    public static DatabaseStatement fromJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        Connection tempConnection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
        Database database;
        try
        {
            DatabaseMetaData metaData = tempConnection.getMetaData();
            String dbName = metaData.getDatabaseProductName().toLowerCase();
            String url = metaData.getURL();
            String user = metaData.getUserName();

            if(dbName.contains("postgresql"))
                database = new PostgresDatabase(url, 0, null, user, null);
            else if(dbName.contains("mysql"))
                database = new MySqlDatabase(url,0, null, user, null);
            else
                throw new UnsupportedOperationException("Unsupported database type: " + dbName);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Failed to determine database type", e);
        }
        finally
        {
            DataSourceUtils.releaseConnection(tempConnection, jdbcTemplate.getDataSource());
        }

        return new DatabaseStatement(database, () -> DataSourceUtils.getConnection(jdbcTemplate.getDataSource())) {};
    }

    public Connection getConnection()
    {
        return connectionSupplier.get();
    }

    @Override
    protected String getKey()
    {
        return "";
    }
}
