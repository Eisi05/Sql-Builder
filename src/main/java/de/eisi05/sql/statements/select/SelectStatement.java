package de.eisi05.sql.statements.select;

import de.eisi05.sql.interfaces.ExecuteQueryStatement;
import de.eisi05.sql.statements.AbstractStatement;
import de.eisi05.sql.statements.AsStatementObject;
import de.eisi05.sql.statements.Case.CaseStatement;
import de.eisi05.sql.statements.FromStatement;
import de.eisi05.sql.statements.IntoStatement;
import de.eisi05.sql.utils.OrmUtils;

import java.util.Arrays;

/**
 * Represents a standard SQL SELECT statement used to select data from a database.
 */
public class SelectStatement extends AbstractStatement
        implements FromStatement.FromStatementContainer, IntoStatement.IntoStatementContainer,
                   CaseStatement.CaseStatementContainer, ExecuteQueryStatement
{
    private final String[] keys;

    /**
     * Constructs a new SelectStatement with the specified columns.
     *
     * @param keys the columns to select
     */
    protected SelectStatement(String... keys)
    {
        super(String.join(", ", keys));
        this.keys = keys;
    }

    /**
     * Gets the columns selected by this statement.
     *
     * @return an array of selected column strings
     */
    public String[] getKeys()
    {
        return keys;
    }

    /**
     * Gets the SQL keyword for this statement.
     *
     * @return "SELECT"
     */
    @Override
    protected String getKey()
    {
        return "SELECT";
    }

    /**
     * Interface for containers that can create standard SELECT statements.
     */
    public interface SelectStatementContainer extends StatementContainer
    {
        /**
         * Creates a standard SELECT statement with the specified column names.
         *
         * @param keys the columns to select
         * @return a new SelectStatement
         */
        default SelectStatement select(String... keys)
        {
            return create(new SelectStatement(keys));
        }

        /**
         * Creates a standard SELECT statement with the specified alias statement objects.
         *
         * @param keys the statement objects representing columns
         * @return a new SelectStatement
         */
        default SelectStatement select(AsStatementObject... keys)
        {
            return create(new SelectStatement(Arrays.stream(keys)
                    .map(AsStatementObject::getKey)
                    .toList().toArray(new String[0])));
        }

        /**
         * Creates a standard SELECT statement with explicit alias mapping.
         *
         * @param keys the final alias statement objects
         * @return a new SelectStatement
         */
        default SelectStatement select(AsStatementObject.FinalAsStatementObject... keys)
        {
            return create(new SelectStatement(Arrays.stream(keys)
                    .map(selectObject -> selectObject.key() +
                            (selectObject.as() != null ? " AS " + selectObject.as() : ""))
                    .toList().toArray(new String[0])));
        }

        /**
         * Creates a SELECT * FROM statement mapped automatically from an ORM entity class.
         *
         * @param clazz the entity class to map to a table
         * @return a FromStatement following the wildcard selection
         */
        default FromStatement selectAll(Class<?> clazz)
        {
            return create(new SelectStatement("*")).from(OrmUtils.resolveTable(clazz));
        }
    }
}