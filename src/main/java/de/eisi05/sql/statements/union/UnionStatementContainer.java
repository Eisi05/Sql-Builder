package de.eisi05.sql.statements.union;

import de.eisi05.sql.statements.AbstractStatement;

public interface UnionStatementContainer extends AbstractStatement.StatementContainer
{
    default UnionStatement union()
    {
        return create(new UnionStatement("UNION"));
    }

    default UnionStatement unionAll()
    {
        return create(new UnionStatement("UNION ALL"));
    }
}
