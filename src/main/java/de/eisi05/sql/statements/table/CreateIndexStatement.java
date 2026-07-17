package de.eisi05.sql.statements.table;

import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.AbstractStatement;

/**
 * Represents a SQL CREATE INDEX statement used to speed up search queries on tables.
 */
public class CreateIndexStatement extends AbstractStatement implements OnIndexStatement.OnIndexStatementContainer,
                                                                       ExecuteUpdateStatement
{
    private final boolean unique;

    /**
     * Constructs a new CreateIndexStatement with unique and index settings.
     *
     * @param index  the name of the index to create
     * @param unique true if this is a UNIQUE index, false otherwise
     */
    protected CreateIndexStatement(String index, boolean unique)
    {
        super(index);
        this.unique = unique;
    }

    /**
     * Gets the SQL keyword representing this index construction statement.
     *
     * @return "CREATE INDEX" or "CREATE UNIQUE INDEX" depending on unique configurations
     */
    @Override
    protected String getKey()
    {
        return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX";
    }

    /**
     * Interface for containers that can construct indices on tables.
     */
    public interface CreateIndexStatementContainer extends StatementContainer
    {
        /**
         * Prepares a standard SQL CREATE INDEX statement.
         *
         * @param index the name of the index to create
         * @return a new CreateIndexStatement
         */
        default CreateIndexStatement createIndex(String index)
        {
            return create(new CreateIndexStatement(index, false));
        }

        /**
         * Prepares a unique SQL CREATE UNIQUE INDEX statement.
         *
         * @param index the name of the unique index to create
         * @return a new CreateIndexStatement
         */
        default CreateIndexStatement createUniqueIndex(String index)
        {
            return create(new CreateIndexStatement(index, true));
        }
    }
}