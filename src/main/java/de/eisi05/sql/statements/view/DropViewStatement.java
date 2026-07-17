package de.eisi05.sql.statements.view;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL DROP VIEW statement used to permanently delete a view from the database.
 */
public class DropViewStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new DropViewStatement targeting the specified view.
     *
     * @param query the target view name
     */
    protected DropViewStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword representing this statement.
     *
     * @return "DROP VIEW"
     */
    @Override
    protected String getKey()
    {
        return "DROP VIEW";
    }

    /**
     * Interface for containers capable of executing a DROP VIEW query.
     */
    public interface DropViewStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Deletes an existing view from the database, safely escaping spaces in the view name.
         *
         * @param name the name of the view to drop
         * @return a new DropViewStatement
         */
        default DropViewStatement dropView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new DropViewStatement(name));
        }
    }
}