package de.eisi05.sql.statements;

import de.eisi05.sql.statements.join.JoinStatementContainer;
import de.eisi05.sql.statements.union.UnionStatementContainer;
import de.eisi05.sql.statements.where.WhereExistsStatement;
import de.eisi05.sql.statements.where.WhereStatement;

import java.util.Arrays;

public class FromStatement extends FinalStatement implements WhereStatement.WhereStatementContainer,
        JoinStatementContainer, UnionStatementContainer, OrderByStatement.OrderByStatementContainer,
        GroupByStatement.GroupByStatementContainer, WhereExistsStatement.WhereExistsStatementContainer,
        FetchFirstStatement.FetchFirstStatementContainer
{
    private FromStatement(String... tables)
    {
        super(String.join(", ", tables));
    }

    @Override
    protected String getKey()
    {
        return "FROM";
    }

    public interface FromStatementContainer extends StatementContainer
    {
        default FromStatement from(String... tables)
        {
            return create(new FromStatement(tables));
        }

        default FromStatement from(AsStatementObject... keys)
        {
            return create(new FromStatement(Arrays.stream(keys)
                    .map(AsStatementObject::getKey)
                    .toList().toArray(new String[0])));
        }

        default FromStatement from(AsStatementObject.FinalAsStatementObject... keys)
        {
            return create(new FromStatement(Arrays.stream(keys)
                    .map(selectObject -> selectObject.key() +
                            (selectObject.as() != null ? " AS " + selectObject.as() : ""))
                    .toList().toArray(new String[0])));
        }
    }
}
