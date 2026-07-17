package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractNotStatement;

/**
 * Represents the <b>{@code WHEN}</b> clause in a <b>{@code CASE}</b> expression. Specifies the condition to evaluate. Supports <b>{@code NOT}</b> modifier and
 * <b>{@code AND/OR}</b> logic operators. Can be followed by conditions or <b>{@code THEN}</b> .
 */
public class CaseWhenStatement extends AbstractNotStatement
        implements CaseConditionStatement.CaseConditionStatementContainer,
                   CaseNotConditionStatement.CaseNotConditionStatementContainer,
                   CaseThenStatement.CaseThenStatementContainer
{
    /**
     * Flag indicating whether <b>{@code NOT}</b> should be applied.
     */
    boolean withNot = false;
    /**
     * The logic operator for chaining conditions (<b>{@code AND/OR}</b> ).
     */
    private LogicOperator logicOperator = null;

    /**
     * Constructs a new CaseWhenStatement with the given value.
     *
     * @param query the <b>{@code WHEN}</b> value
     */
    protected CaseWhenStatement(String query)
    {
        super(query);
    }

    /**
     * Sets the logic operator for this <b>{@code WHEN}</b> statement.
     *
     * @param logicOperator the logic operator (<b>{@code AND}</b> or <b>{@code OR}</b> )
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
     * @return the SQL keyword (<b>{@code WHEN}</b> , <b>{@code AND}</b> , <b>{@code OR}</b> , or with <b>{@code NOT}</b> modifier)
     */
    @Override
    protected String getKey()
    {
        return (logicOperator == null ? "WHEN" : "") + (logicOperator != null ? logicOperator.name() : "") +
                (withNot ? " NOT" : "");
    }
}
