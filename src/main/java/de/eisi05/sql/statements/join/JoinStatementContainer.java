package de.eisi05.sql.statements.join;

/**
 * Interface that aggregates all join statement container interfaces. Provides a single point for accessing all join creation methods (INNER, LEFT, RIGHT, FULL
 * OUTER).
 */
public interface JoinStatementContainer
        extends FullJoinStatement.FullJoinStatementContainer, LeftJoinStatement.LeftJoinStatementContainer,
                RightJoinStatement.RightJoinStatementContainer, InnerJoinStatement.InnerJoinStatementContainer
{
}
