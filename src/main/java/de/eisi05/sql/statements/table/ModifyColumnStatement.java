package de.eisi05.sql.statements.table;

import de.eisi05.sql.database.OracleDatabase;
import de.eisi05.sql.interfaces.SqlDataType;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.FinalStatement;

public class ModifyColumnStatement extends FinalStatement
{
    private final String key;

    protected ModifyColumnStatement(String query, String key)
    {
        super(query);
        this.key = key;
    }

    @Override
    protected String getKey()
    {
        return key + (getDatabaseStatement().getDatabase()
                .map(database -> database instanceof OracleDatabase oracleDatabase && oracleDatabase.getVersion() < 10)
                .orElse(true) ? " COLUMN" : "");
    }

    public interface ModifyColumnStatementContainer extends AbstractStatement.StatementContainer
    {
        default ModifyColumnStatement modify(String key, SqlDataType<?> dataType)
        {
            return create(new ModifyColumnStatement(key + " " + dataType.getName(), "MODIFY"));
        }

        default ModifyColumnStatement alter(String key, SqlDataType<?> dataType)
        {
            return create(new ModifyColumnStatement(key + " " + dataType.getName(), "ALTER"));
        }
    }
}
