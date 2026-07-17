package de.eisi05.sql.statements.where;

import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents an SQL <b>{@code IN}</b> condition fragment evaluating inclusion within a set of values or a subquery.
 */
public class WhereInStatement extends AbstractWhereStatement
{
    /**
     * Constructs a {@code WhereInStatement} with the given collection placeholder fragment.
     *
     * @param query the SQL expression containing placeholders for the collection items
     */
    protected WhereInStatement(String query)
    {
        super(query);
    }

    /**
     * Returns the SQL keyword for membership testing.
     *
     * @return {@code "IN"}
     */
    @Override
    protected String getKey()
    {
        return "IN";
    }

    /**
     * A container interface providing multiple overloaded fluent builder methods for assembling an <b>{@code IN}</b> clause.
     */
    public interface WhereInStatementContainer extends AbstractStatement.StatementContainer
    {
        /**
         * Appends an <b>{@code IN}</b> clause featuring a variable argument list of objects.
         *
         * @param o one or more object parameters matching the column target
         * @return a configured {@link WhereInStatement}
         */
        default WhereInStatement in(Object... o)
        {
            String placeholders = String.join(",", Collections.nCopies(o.length, "?"));
            return create(new WhereInStatement("(" + placeholders + ")"), o);
        }

        /**
         * Appends an <b>{@code IN}</b> clause specialized for raw primitive integer arrays. Handles auto-boxing internally.
         *
         * @param ids an array of primitive integer elements
         * @return a configured {@link WhereInStatement}
         */
        default WhereInStatement in(int[] ids)
        {
            String placeholders = String.join(",", Collections.nCopies(ids.length, "?"));
            Object[] boxedIds = Arrays.stream(ids).boxed().toArray();
            return create(new WhereInStatement("(" + placeholders + ")"), boxedIds);
        }

        /**
         * Appends an <b>{@code IN}</b> clause mapping elements from a standard Java collection type into parameters.
         *
         * @param collection a collection of elements representing the parameter list
         * @return a configured {@link WhereInStatement}
         */
        default WhereInStatement in(Collection<?> collection)
        {
            String placeholders = String.join(",", Collections.nCopies(collection.size(), "?"));
            return create(new WhereInStatement("(" + placeholders + ")"), collection);
        }

        /**
         * Appends an <b>{@code IN}</b> clause mapping evaluated fields against an entire embedded subquery.
         *
         * @param finalStatement the statement representing the subquery source
         * @return a configured {@link WhereInStatement}
         */
        default WhereInStatement in(FinalStatement finalStatement)
        {
            return create(new WhereInStatement("(" + finalStatement.getQuery() + ")"));
        }
    }
}