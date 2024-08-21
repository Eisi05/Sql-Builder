package de.eisi05.sql.statements.where;

public interface WhereDefaultStatementContainer extends WhereBetweenStatement.WhereBetweenStatementContainer,
        WhereNotBetweenStatement.WhereNotBetweenStatementContainer, WhereEqualStatement.WhereEqualStatementContainer,
        WhereNotEqualStatement.WhereNotEqualStatementContainer,
        WhereGreaterThanStatement.WhereGreaterThanStatementContainer,
        WhereNotGreaterThanStatement.WhereNotGreaterStatementContainer,
        WhereGreaterThanOrEqual.WhereGreaterThanOrEqualStatementContainer,
        WhereInStatement.WhereInStatementContainer, WhereNotInStatement.WhereNotInStatementContainer,
        WhereIsNullStatement.WhereIsNullStatementContainer, WhereNotIsNullStatement.WhereNotIsNullStatementContainer,
        WhereLessThanStatement.WhereLessThanStatementContainer,
        WhereNotLessThanStatement.WhereNotLessStatementContainer,
        WhereLessThanOrEqualStatement.WhereLessOrEqualThanStatementContainer,
        WhereLikeStatement.WhereLikeStatementContainer,
        WhereNotLikeStatement.WhereNotLikeStatementContainer, WhereAnyStatement.WhereAnyStatementContainer,
        WhereAllStatement.WhereAllStatementContainer
{
}
