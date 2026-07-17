package de.eisi05.sql.statements.where;

/**
 * A composite interface that bundles all standard <b>{@code WHERE}</b> condition containers into a unified entry point.
 * <p>
 * Implementing classes gain access to a comprehensive suite of fluent SQL operations such as <b>{@code EQUAL}</b> , <b>{@code BETWEEN}</b> , <b>{@code INT}</b>
 * , <b>{@code ANY}</b> , <b>{@code ALL}</b> , etc.
 * </p>
 */
public interface WhereDefaultStatementContainer extends WhereBetweenStatement.WhereBetweenStatementContainer,
                                                        WhereNotBetweenStatement.WhereNotBetweenStatementContainer,
                                                        WhereEqualStatement.WhereEqualStatementContainer,
                                                        WhereNotEqualStatement.WhereNotEqualStatementContainer,
                                                        WhereGreaterThanStatement.WhereGreaterThanStatementContainer,
                                                        WhereNotGreaterThanStatement.WhereNotGreaterStatementContainer,
                                                        WhereGreaterThanOrEqual.WhereGreaterThanOrEqualStatementContainer,
                                                        WhereInStatement.WhereInStatementContainer, WhereNotInStatement.WhereNotInStatementContainer,
                                                        WhereIsNullStatement.WhereIsNullStatementContainer,
                                                        WhereNotIsNullStatement.WhereNotIsNullStatementContainer,
                                                        WhereLessThanStatement.WhereLessThanStatementContainer,
                                                        WhereNotLessThanStatement.WhereNotLessStatementContainer,
                                                        WhereLessThanOrEqualStatement.WhereLessOrEqualThanStatementContainer,
                                                        WhereLikeStatement.WhereLikeStatementContainer,
                                                        WhereNotLikeStatement.WhereNotLikeStatementContainer, WhereAnyStatement.WhereAnyStatementContainer,
                                                        WhereAllStatement.WhereAllStatementContainer
{
}