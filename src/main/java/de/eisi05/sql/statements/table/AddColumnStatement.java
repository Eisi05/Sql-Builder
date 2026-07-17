package de.eisi05.sql.statements.table;

import de.eisi05.sql.database.PostgresDatabase;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL <b>{@code ADD COLUMN}</b> statement used within an <b>{@code ALTER TABLE}</b> block.
 */
public class AddColumnStatement extends FinalStatement
{
    /**
     * Constructs a new AddColumnStatement with the specified column query.
     *
     * @param query the partial query representing the column definition
     */
    protected AddColumnStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "ADD"
     */
    @Override
    protected String getKey()
    {
        return "ADD";
    }

    /**
     * Interface for containers that can append <b>{@code ADD COLUMN}</b> clauses to alter table actions.
     */
    public interface AddColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Adds a column definition to the target table.
         *
         * @param column the table column model to add
         * @return a new AddColumnStatement
         */
        default AddColumnStatement add(TableColumn column)
        {
            return create(new AddColumnStatement(column.asQuery(getDatabase()
                    .map(database -> database instanceof PostgresDatabase).orElse(false))));
        }
    }
}