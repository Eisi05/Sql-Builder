package de.eisi05.sql.statements.select;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.AsStatementObject;
import de.eisi05.sql.statements.Case.CaseStatement;
import de.eisi05.sql.statements.FromStatement;
import de.eisi05.sql.statements.IntoStatement;

import java.util.Arrays;

public class SelectStatement extends AbstractStatement
        implements FromStatement.FromStatementContainer, IntoStatement.IntoStatementContainer,
        CaseStatement.CaseStatementContainer, ExecuteQueryStatement
{
    private final String[] keys;

    protected SelectStatement(String... keys)
    {
        super(String.join(", ", keys));
        this.keys = keys;
    }

    public String[] getKeys()
    {
        return keys;
    }

    @Override
    protected String getKey()
    {
        return "SELECT";
    }

    public interface SelectStatementContainer extends StatementContainer
    {
        default SelectStatement select(String... keys)
        {
            return create(new SelectStatement(keys));
        }

        default SelectStatement select(AsStatementObject... keys)
        {
            return create(new SelectStatement(Arrays.stream(keys)
                    .map(AsStatementObject::getKey)
                    .toList().toArray(new String[0])));
        }

        default SelectStatement select(AsStatementObject.FinalAsStatementObject... keys)
        {
            return create(new SelectStatement(Arrays.stream(keys)
                    .map(selectObject -> selectObject.key() +
                            (selectObject.as() != null ? " AS " + selectObject.as() : ""))
                    .toList().toArray(new String[0])));
        }
    }
}
