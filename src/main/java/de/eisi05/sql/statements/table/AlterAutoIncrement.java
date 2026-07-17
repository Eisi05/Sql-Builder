package de.eisi05.sql.statements.table;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL <b>{@code AUTO_INCREMENT ALTER}</b> statement used to modify starting values.
 */
public class AlterAutoIncrement extends FinalStatement
{
    /**
     * Constructs a new AlterAutoIncrement statement with the specified starting increment query.
     *
     * @param query the partial query indicating the starting increment value
     */
    protected AlterAutoIncrement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "AUTO_INCREMENT="
     */
    @Override
    protected String getKey()
    {
        return "AUTO_INCREMENT=";
    }

    /**
     * Interface for containers that can configure starting <b>{@code AUTO-INCREMENT}</b> configurations.
     */
    public interface AlterAutoIncrementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Alters the starting <b>{@code AUTO-INCREMENT}</b> value.
         *
         * @param startIncrement the integer starting sequence value
         * @return a new AlterAutoIncrement
         */
        default AlterAutoIncrement autoIncrement(int startIncrement)
        {
            return create(new AlterAutoIncrement(String.valueOf(startIncrement)));
        }
    }
}