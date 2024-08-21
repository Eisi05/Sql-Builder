package de.eisi05.sql.statements.Case;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractNotStatement;

public class CaseWhenStatement extends AbstractNotStatement
        implements CaseConditionStatement.CaseConditionStatementContainer,
        CaseNotConditionStatement.CaseNotConditionStatementContainer
{
    boolean withNot = false;
    private LogicOperator logicOperator = null;

    protected CaseWhenStatement(String query)
    {
        super(query);
    }

    CaseWhenStatement withOperation(LogicOperator logicOperator)
    {
        this.logicOperator = logicOperator;
        return this;
    }

    @Override
    protected String getKey()
    {
        return (logicOperator == null ? "WHEN" : "") + (logicOperator != null ? logicOperator.name() : "") +
                (withNot ? " NOT" : "");
    }

    public interface CaseWhenStatementContainer extends StatementContainer
    {
        default CaseWhenStatement when(String key)
        {
            return create(new CaseWhenStatement(key));
        }
    }
}
