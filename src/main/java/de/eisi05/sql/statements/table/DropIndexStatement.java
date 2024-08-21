package de.eisi05.sql.statements.table;

import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

public class DropIndexStatement extends FinalStatement implements ExecuteUpdateStatement
{
    protected DropIndexStatement(String query)
    {
        super(query);
    }

    @Override
    protected String getKey()
    {
        return "DROP INDEX";
    }

    public interface DropIndexStatementContainer extends StatementContainer
    {
        default DropIndexStatement dropIndex(String index, String table)
        {
            return getDatabase().map(database ->
            {
                if(database.getDatabaseType() == DatabaseType.SQL_SERVER)
                    return create(new DropIndexStatement(table + "." + index));
                else if(database.getDatabaseType() == DatabaseType.MS_ACCESS)
                    return create(new DropIndexStatement(index + " ON " + table));
                throw new UnsupportedOperationException(
                        database.getDatabaseType().name() + " does not support this method!");
            }).orElseThrow(() -> new UnsupportedOperationException("No database connected!"));
        }

        default DropIndexStatement dropIndex(String index)
        {
            return getDatabase().map(database ->
            {
                if(database.getDatabaseType() == DatabaseType.ORACLE)
                    return create(new DropIndexStatement(index));
                else if(database.getDatabaseType() == DatabaseType.MYSQL)
                {
                    if(this instanceof AlterTableStatement)
                        return create(new DropIndexStatement(index));
                    throw new UnsupportedOperationException(database.getDatabaseType().name() +
                            " needs an alter table statement before using this method!");
                }
                throw new UnsupportedOperationException(
                        database.getDatabaseType().name() + " does not support this method!");
            }).orElseThrow(() -> new UnsupportedOperationException("No database connected!"));
        }
    }
}
