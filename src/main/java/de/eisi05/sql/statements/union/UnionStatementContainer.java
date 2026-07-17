package de.eisi05.sql.statements.union;

import de.eisi05.sql.statements.AbstractStatement;

/**
 * Interface for containers that can construct and chain UNION or UNION ALL statements.
 */
public interface UnionStatementContainer extends AbstractStatement.StatementContainer
{
    /**
     * Appends a standard UNION clause to combine query results, removing duplicate rows.
     *
     * @return a new UnionStatement initialized with "UNION"
     */
    default UnionStatement union()
    {
        return create(new UnionStatement("UNION"));
    }

    /**
     * Appends a UNION ALL clause to combine query results, including duplicate rows.
     *
     * @return a new UnionStatement initialized with "UNION ALL"
     */
    default UnionStatement unionAll()
    {
        return create(new UnionStatement("UNION ALL"));
    }
}