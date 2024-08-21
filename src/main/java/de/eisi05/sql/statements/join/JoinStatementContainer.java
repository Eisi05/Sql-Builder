package de.eisi05.sql.statements.join;

public interface JoinStatementContainer
        extends FullJoinStatement.FullJoinStatementContainer, LeftJoinStatement.LeftJoinStatementContainer,
        RightJoinStatement.RightJoinStatementContainer, InnerJoinStatement.InnerJoinStatementContainer
{
}
