package de.eisi05.sql.statements.where;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractNotStatement;

/**
 * Represents the primary entry point or structural placeholder for building an SQL <b>{@code WHERE}</b> clause.
 * <p>
 * This class handles the initialization of the clause and manages subsequent logic chaining operators like <b>{@code AND}</b> , <b>{@code OR}</b> , and
 * structural negations.
 * </p>
 */
public class WhereStatement extends AbstractNotStatement implements WhereDefaultStatementContainer
{
    /**
     * The logical operator connecting this statement fragment to previous fragments (e.g., <b>{@code AND}</b> , <b>{@code OR}</b> ).
     */
    protected LogicOperator logicOperator = null;

    /**
     * Constructs a base {@code WhereStatement} targeting the provided column or conditional context expression.
     *
     * @param query the column name or structural key expression for the condition
     */
    protected WhereStatement(String query)
    {
        super(query);
    }

    /**
     * Attaches a logical connecting operator to this statement fragment.
     *
     * @param logicOperator the connector token to attach (e.g., <b>{@code AND}</b> , <b>{@code OR}</b> )
     * @return this updated instance for builder chaining
     */
    WhereStatement withOperation(LogicOperator logicOperator)
    {
        this.logicOperator = logicOperator;
        return this;
    }

    /**
     * Computes the specific SQL grammar prefix keyword configuration dynamically based on active logical operations.
     *
     * @return an SQL keyword segment string such as "WHERE", "AND", "OR NOT", or variations
     */
    @Override
    protected String getKey()
    {
        return (logicOperator == null ? "WHERE" : "") + (logicOperator != null ? logicOperator.name() : "") +
                (withNot ? " NOT" : "");
    }

    /**
     * A container interface providing the root entry builder method to spawn an initial <b>{@code WHERE}</b> clause.
     */
    public interface WhereStatementContainer extends StatementContainer
    {
        /**
         * Initializes a new root <b>{@code WHERE}</b> clause constraint.
         *
         * @param key the database column or base expression context
         * @return a freshly created {@link WhereStatement} configuration hook
         */
        default WhereStatement where(String key)
        {
            return create(new WhereStatement(key));
        }
    }
}