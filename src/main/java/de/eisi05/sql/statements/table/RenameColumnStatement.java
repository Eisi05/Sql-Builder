package de.eisi05.sql.statements.table;

import de.eisi05.sql.annotations.SqlData;
import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

@SqlData(value = {DatabaseType.MYSQL, DatabaseType.MS_ACCESS, DatabaseType.ORACLE}, oracleVersion = 10)
public class RenameColumnStatement extends FinalStatement
{
    protected RenameColumnStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "RENAME";
    }

    public interface RenameColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        default RenameColumnStatement renameColumn(String key, String to)
        {
            return create(new RenameColumnStatement(key + " TO " + to));
        }
    }
}
