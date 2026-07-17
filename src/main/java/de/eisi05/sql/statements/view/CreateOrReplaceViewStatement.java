package de.eisi05.sql.statements.view;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL CREATE OR REPLACE VIEW statement used to define or modify an existing virtual table schema.
 */
public class CreateOrReplaceViewStatement extends CreateViewStatement
{
    /**
     * Constructs a new CreateOrReplaceViewStatement targeting the specified view name.
     *
     * @param name the name of the view to create or update
     */
    protected CreateOrReplaceViewStatement(String name)
    {
        super(name);
    }

    /**
     * Gets the SQL keyword representing this statement.
     *
     * @return "CREATE OR REPLACE VIEW"
     */
    @Override
    protected String getKey()
    {
        return "CREATE OR REPLACE VIEW";
    }

    /**
     * Interface for containers capable of executing a CREATE OR REPLACE VIEW query.
     */
    public interface CreateOrReplaceViewStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Initiates a CREATE OR REPLACE VIEW command, safely escaping spaces in the view name.
         *
         * @param name the name of the target view
         * @return a new CreateOrReplaceViewStatement
         */
        default CreateOrReplaceViewStatement createOrReplaceView(String name)
        {
            if(name.contains(" "))
                name = "[" + name + "]";

            return create(new CreateOrReplaceViewStatement(name));
        }
    }
}