package de.eisi05.sql.statements;

import de.eisi05.sql.database.Database;
import de.eisi05.sql.database.MySqlDatabase;
import de.eisi05.sql.database.PostgresDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Abstract base class for database-specific statements. Represents the root of a SQL statement chain and provides isolated database connection management. It
 * supports delegating connection lifecycles to dynamic providers, enabling native compatibility with both custom stand-alone connection management and
 * transactional platforms like Spring Framework's context.
 */
public abstract class DatabaseStatement extends AbstractStatement
        implements AbstractStatement.DefaultStatementContainers
{
    /**
     * The database dialect and connectivity configuration.
     */
    protected final Database database;
    /**
     * Supplier for obtaining new or thread-bound database connections.
     */
    private final Supplier<Connection> connectionSupplier;
    /**
     * Callback engine handler responsible for safely freeing up or releasing a transactional connection.
     */
    private final Consumer<Connection> connectionReleaser;

    /**
     * Constructs a new DatabaseStatement with the specified database engine meta-profile, connection acquiring routine, and corresponding resource disposal
     * action.
     *
     * @param database           the database configuration
     * @param connectionSupplier the supplier for acquiring database connections
     * @param connectionReleaser the handler logic used to dispose or release connections back to their pool
     */
    protected DatabaseStatement(Database database, Supplier<Connection> connectionSupplier, Consumer<Connection> connectionReleaser)
    {
        super(null);
        this.database = database;
        this.connectionSupplier = connectionSupplier;
        this.connectionReleaser = connectionReleaser;
    }

    /**
     * Creates a DatabaseStatement from a Spring {@link JdbcTemplate}. Automatically detects the underlying database engine (PostgreSQL or MySQL) from the live
     * metadata connection, and sets up transaction-aware connection routing utilizing {@link DataSourceUtils}.
     *
     * @param jdbcTemplate the Spring JdbcTemplate containing the datasource instance to inspect and wrap
     * @return a new DatabaseStatement configured for the explicitly detected database engine
     * @throws RuntimeException if the database dialect is unsupported or if metadata queries fail
     */
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
                database = new MySqlDatabase(url, 0, null, user, null);
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

        return new DatabaseStatement(database, () -> DataSourceUtils.getConnection(jdbcTemplate.getDataSource()),
                (connection) -> DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource())) {};
    }

    /**
     * Creates a rigid standalone DatabaseStatement backed by a constant raw JDBC connection context. Closes the raw physical connection upon operational
     * completion only if auto-commit mode is explicitly active.
     *
     * @param database   the target database configuration metadata mapping
     * @param connection the open persistent active JDBC connection resource
     * @return a new DatabaseStatement mapping queries natively through the supplied connection context
     */
    public static DatabaseStatement fromConnection(Database database, Connection connection)
    {
        return new DatabaseStatement(database, () -> connection, (conn) ->
        {
            try
            {
                if(conn != null && !conn.isClosed() && conn.getAutoCommit())
                    conn.close();
            }
            catch(Exception e) {}
        }) {};
    }

    /**
     * Dispatches an allocated connection to the internal release handler callback. This ensures transactional architectures like Spring can safely preserve a
     * connection thread-state for multiple statements, whereas simple environments can discard it instantly.
     *
     * @param connection the connection instance needing contextual cleanup or release
     */
    public void releaseConnection(Connection connection)
    {
        if(connectionReleaser != null && connection != null)
            connectionReleaser.accept(connection);
    }

    /**
     * Gets a database connection context from the connection supplier rule.
     *
     * @return an open database connection prepared for active query assignment
     */
    public Connection getConnection()
    {
        return connectionSupplier.get();
    }

    /**
     * Gets the SQL keyword for this statement. Returns an empty string since database root context wrapper statements do not have a SQL keyword prefix.
     *
     * @return an empty string
     */
    @Override
    protected String getKey()
    {
        return "";
    }
}