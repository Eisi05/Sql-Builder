package de.eisi05.sql.statements.union;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.select.SelectStatement;

/**
 * Represents a SQL UNION or UNION ALL operation used to combine the result sets of multiple queries.
 */
public class UnionStatement extends AbstractStatement implements SelectStatement.SelectStatementContainer
{
    /**
     * Constructs a new UnionStatement with the specified union query modifier.
     *
     * @param query the union query operator expression (e.g., "UNION" or "UNION ALL")
     */
    protected UnionStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword representing this statement.
     *
     * @return an empty string, as the operation keyword is defined by the query constructor parameter
     */
    @Override
    protected String getKey()
    {
        return "";
    }
}