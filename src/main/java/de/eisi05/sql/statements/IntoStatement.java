package de.eisi05.sql.statements;

/**
 * Represents a SQL <b>{@code INTO}</b> clause. Used in <b>{@code INSERT}</b> statements to specify the target table. Also used in
 * <b>{@code SELECT ... INTO}</b> statements for table creation.
 */
public class IntoStatement extends AbstractStatement implements InStatement.InStatementContainer,
                                                                FromStatement.FromStatementContainer
{
    /**
     * Constructs a new IntoStatement for the specified table.
     *
     * @param table the table name
     */
    protected IntoStatement(String table)
    {
        super(table);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "INTO"
     */
    @Override
    protected String getKey()
    {
        return "INTO";
    }

    /**
     * Interface for containers that can create <b>{@code INTO}</b> statements.
     */
    public interface IntoStatementContainer extends StatementContainer
    {
        /**
         * Creates an <b>{@code INTO}</b> statement for the specified table.
         *
         * @param table the table name
         * @return a new IntoStatement
         */
        default IntoStatement into(String table)
        {
            return create(new IntoStatement(table));
        }
    }
}
