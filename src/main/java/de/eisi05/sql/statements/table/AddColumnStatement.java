package de.eisi05.sql.statements.table;

import de.eisi05.sql.database.PostgresDatabase;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL ADD COLUMN statement used within an ALTER TABLE block.
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
     * Interface for containers that can append ADD COLUMN clauses to alter table actions.
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