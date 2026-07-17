package de.eisi05.sql.statements.table;

import de.eisi05.sql.enums.DatabaseType;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

/**
 * Represents a SQL <b>{@code DROP INDEX}</b> statement to remove indexes from tables.
 */
public class DropIndexStatement extends FinalStatement implements ExecuteUpdateStatement
{
    /**
     * Constructs a new DropIndexStatement with the target query structure.
     *
     * @param query the target structural index query details
     */
    protected DropIndexStatement(String query)
    {
        super(query);
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "DROP INDEX"
     */
    @Override
    protected String getKey()
    {
        return "DROP INDEX";
    }

    /**
     * Interface for containers that can drop table indexes.
     */
    public interface DropIndexStatementContainer extends StatementContainer
    {
        /**
         * Drops an index using the database-specific query dialect for dual index/table configurations.
         *
         * @param index the name of the index
         * @param table the name of the table hosting the index
         * @return a new DropIndexStatement configured for the active dialect
         * @throws UnsupportedOperationException if database type is unsupported or if no database is connected
         */
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

        /**
         * Drops an index based solely on its identifier.
         *
         * @param index the name of the index
         * @return a new DropIndexStatement
         * @throws UnsupportedOperationException if database type is unsupported or requires alter statements
         */
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