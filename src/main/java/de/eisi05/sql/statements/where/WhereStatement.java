package de.eisi05.sql.statements.where;

import de.eisi05.sql.enums.LogicOperator;
import de.eisi05.sql.statements.AbstractNotStatement;

public class WhereStatement extends AbstractNotStatement implements WhereDefaultStatementContainer
{
    protected LogicOperator logicOperator = null;

    protected WhereStatement(String query)
    {
        super(query);
    }

    WhereStatement withOperation(LogicOperator logicOperator)
    {
        this.logicOperator = logicOperator;
        return this;
    }

    @Override
    protected String getKey()
    {
        return (logicOperator == null ? "WHERE" : "") + (logicOperator != null ? logicOperator.name() : "") +
                (withNot ? " NOT" : "");
    }

    public interface WhereStatementContainer extends StatementContainer
    {
        default WhereStatement where(String key)
        {
            return create(new WhereStatement(key));
        }
    }
}
