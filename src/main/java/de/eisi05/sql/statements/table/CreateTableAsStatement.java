package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatement;

/**
 * Represents a SQL <b>{@code CREATE TABLE AS}</b> select-statement variant.
 */
public class CreateTableAsStatement extends AbstractStatement implements SelectStatement.SelectStatementContainer
{
    /**
     * Constructs a new CreateTableAsStatement.
     *
     * @param query the query snippet representing the <b>{@code AS}</b> criteria
     */
    protected CreateTableAsStatement(String query)
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

    /**
     * Interface for containers that can transition tables using an <b>{@code AS}</b> criteria block.
     */
    public interface CreateTableAsStatementContainer extends StatementContainer
    {
        /**
         * Transitions to selecting details to construct a table.
         *
         * @return a new CreateTableAsStatement representing "AS"
         */
        default CreateTableAsStatement as()
        {
            return create(new CreateTableAsStatement("AS"));
        }
    }
}