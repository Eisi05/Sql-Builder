package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractNotStatement;

/**
 * Represents the WHEN clause in a CASE expression. Specifies the condition to evaluate. Supports NOT modifier and AND/OR logic operators. Can be followed by
 * conditions or THEN.
 */
public class CaseWhenStatement extends AbstractNotStatement
        implements CaseConditionStatement.CaseConditionStatementContainer,
                   CaseNotConditionStatement.CaseNotConditionStatementContainer,
                   CaseThenStatement.CaseThenStatementContainer
{
    /**
     * Flag indicating whether NOT should be applied.
     */
    boolean withNot = false;
    /**
     * The logic operator for chaining conditions (AND/OR).
     */
    private LogicOperator logicOperator = null;

    /**
     * Constructs a new CaseWhenStatement with the given value.
     *
     * @param query the WHEN value
     */
    protected CaseWhenStatement(String query)
    {
        super(query);
    }

    /**
     * Sets the logic operator for this WHEN statement.
     *
     * @param logicOperator the logic operator (AND or OR)
     * @return this statement for chaining
     */
    CaseWhenStatement withOperation(LogicOperator logicOperator)
    {
        this.logicOperator = logicOperator;
        return this;
    }

    /**
     * Gets the SQL keyword for this statement. Returns "WHEN" for the first condition, or the logic operator for chained conditions. Appends "NOT" if the
     * withNot flag is set.
     *
     * @return the SQL keyword (WHEN, AND, OR, or with NOT modifier)
     */
    @Override
    protected String getKey()
    {
        return (logicOperator == null ? "WHEN" : "") + (logicOperator != null ? logicOperator.name() : "") +
                (withNot ? " NOT" : "");
    }
}
