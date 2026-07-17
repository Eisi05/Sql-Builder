package de.eisi05.sql.statements.join;

/**
 * Interface that aggregates all join statement container interfaces. Provides a single point for accessing all join creation methods (<b>{@code INNER}</b> ,
 * <b>{@code LEFT}</b> , <b>{@code RIGHT}</b> , <b>{@code FULL}</b>, <b>{@code OUTER}</b> ).
 */
public interface JoinStatementContainer
        extends FullJoinStatement.FullJoinStatementContainer, LeftJoinStatement.LeftJoinStatementContainer,
                RightJoinStatement.RightJoinStatementContainer, InnerJoinStatement.InnerJoinStatementContainer
{
}
