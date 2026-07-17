package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Collections;

/**
 * Represents an SQL <b>{@code NOT IN}</b> membership exclusion condition fragment.
 */
public class WhereNotInStatement extends WhereInStatement implements WhereNotStatement
{
    /**
     * Constructs a {@code WhereNotInStatement} with the given collection placeholder fragment.
     *
     * @param query the SQL expression containing placeholders for the excluded items
     */
    protected WhereNotInStatement(String query)
    {
        super(query);
    }

    /**
     * Defines whether the keyword <b>{@code NOT}</b> should immediately follow the word <b>{@code WHERE}</b> structurally.
     *
     * @return {@code false} since <b>{@code NOT}</b> is grouped directly within the <b>{@code NOT IN}</b> phrase
     */
    @Override
    public boolean isNotAfterWhere()
    {
        return false;
    }

    /**
     * A container interface providing overloaded fluent builder methods for assembling a <b>{@code NOT IN}</b> clause.
     */
    public interface WhereNotInStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends a <b>{@code NOT IN}</b> clause featuring a variable argument list of objects.
         *
         * @param o one or more object parameters to be excluded
         * @return a configured {@link WhereNotInStatement}
         */
        default WhereNotInStatement notIn(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new WhereNotInStatement("(" + placeholders + ")"), o);
        }

        /**
         * Appends a <b>{@code NOT IN}</b> clause mapping excluded fields against an entire embedded subquery.
         *
         * @param finalStatement the statement representing the subquery exclusion source
         * @return a configured {@link WhereNotInStatement}
         */
        default WhereNotInStatement notIn(FinalStatement finalStatement)
        {
            return create(new WhereNotInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}