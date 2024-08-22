package de.eisi05.sql.statements.table;

import de.eisi05.sql.exceptions.PrimaryKeyException;
import de.eisi05.sql.interfaces.ExecuteUpdateStatement;
import de.eisi05.sql.statements.FinalStatement;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateTableStatement extends FinalStatement
        implements CreateTableAsStatement.CreateTableAsStatementContainer, ExecuteUpdateStatement
{
    protected CreateTableStatement(String table)
    {
        super(table);
    }

    @Override
    protected String getKey()
    {
        return "CREATE TABLE";
    }

    public interface CreateTableStatementContainer extends StatementContainer
    {
        default CreateTableStatement createTable(String table, TableColumn column, TableColumn... columns)
        {
            columns = Stream.concat(Arrays.stream(columns), Stream.of(column)).toArray(TableColumn[]::new);

            if(Arrays.stream(columns).filter(TableColumn::isPrimaryKey).count() > 1)
                throw new PrimaryKeyException("Cannot have more than one primary key in a table");

            return create(new CreateTableStatement(
                    table + " (" + Arrays.stream(columns).map(TableColumn::asQuery)
                            .collect(Collectors.joining(", ")) + ")"));
        }

        default CreateTableStatement createTable(String table)
        {
            return create(new CreateTableStatement(table));
        }

        default CreateTableStatement createTableIfNotExists(String table, TableColumn column, TableColumn... columns)
        {
            columns = Stream.concat(Arrays.stream(columns), Stream.of(column)).toArray(TableColumn[]::new);

            if(Arrays.stream(columns).filter(TableColumn::isPrimaryKey).count() > 1)
                throw new PrimaryKeyException("Cannot have more than one primary key in a table");

            return create(new CreateTableStatement("IF NOT EXISTS " +
                    table + " (" + Arrays.stream(columns).map(TableColumn::asQuery)
                    .collect(Collectors.joining(", ")) + ")"));
        }
    }
}
