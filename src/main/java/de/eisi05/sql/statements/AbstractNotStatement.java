package de.eisi05.sql.statements;

/**
 * Abstract base class for statements that support a <b>{@code NOT}</b> modifier. Extends {@link AbstractStatement} and adds support for negating the
 * statement.
 */
public abstract class AbstractNotStatement extends AbstractStatement
{
    /**
     * Flag indicating whether the <b>{@code NOT}</b> modifier should be applied to this statement.
     */
    protected boolean withNot = false;

    /**
     * Constructs a new AbstractNotStatement with the given query.
     *
     * @param query the SQL query fragment for this statement
     */
    protected AbstractNotStatement(String query)
    {
        super(query);
    }
}
