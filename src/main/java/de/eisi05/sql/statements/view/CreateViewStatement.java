package de.eisi05.sql.statements.view;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatementContainer;

/**
 * Represents a SQL <b>{@code CREATE VIEW}</b> statement to establish a virtual table based on a query result.
 */
public class CreateViewStatement extends AbstractStatement implements AbstractStatement.StatementContainer,
                                                                      ExecuteUpdateStatement
{
    /**
     * Constructs a new CreateViewStatement targeting the specified view name.
     *
     * @param name the name of the view to create
     */
    protected CreateViewStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword representing this statement.
     *
     * @return "CREATE VIEW"
     */
    @Override
    protected String getKey()
    {
        return "CREATE VIEW";
    }

    /**
     * Transitions to an <b>{@code AS}</b> statement block allowing selection definitions for this view.
     *
     * @return a new CreateViewAsStatement representing "AS"
     */
    public CreateViewAsStatement as()
    {
        return create(new CreateViewAsStatement("AS"));
    }

    /**
     * Interface for containers capable of building <b>{@code CREATE VIEW}</b> statements.
     */
    public interface CreateViewStatementContainer extends StatementContainer
    {
        /**
         * Initiates a standard <b>{@code CREATE VIEW}</b> command, safely escaping spaces in the view name.
         *
         * @param name the name of the target view
         * @return a new CreateViewStatement
         */
        default CreateViewStatement createView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new CreateViewStatement(name));
        }
    }

    /**
     * Represents the SQL <b>{@code AS}</b> clause linked immediately after a <b>{@code CREATE VIEW}</b> statement.
     */
    public static class CreateViewAsStatement extends AbstractStatement implements SelectStatementContainer
    {
        /**
         * Constructs a new CreateViewAsStatement.
         *
         * @param query the query snippet representing the <b>{@code AS}</b> keyword
         */
        protected CreateViewAsStatement(String query)
        {
            super(query);
        }

        /**
         * Gets the SQL keyword modifier.
         *
         * @return an empty string
         */
        @Override
        protected String getKey()
        {
            return "";
        }
    }
}