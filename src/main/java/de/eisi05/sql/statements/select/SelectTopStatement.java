package de.eisi05.sql.statements.select;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;

@SqlData({DatabaseType.SQL_SERVER, DatabaseType.MS_ACCESS})
public class SelectTopStatement extends SelectStatement
{
    private SelectTopStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "SELECT TOP";
    }

    public interface SelectTopStatementContainer extends StatementContainer
    {
        default SelectTopStatement selectTop(int amount, String... keys)
        {
            return create(new SelectTopStatement(amount + " " + String.join(", ", keys)));
        }

        default SelectTopStatement selectTopPercent(int percent, String... keys)
        {
            return create(new SelectTopStatement(percent + " PERCENT " + String.join(", ", keys)));
        }
    }
}
