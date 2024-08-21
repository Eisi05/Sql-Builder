package de.eisi05.sql.statements.select;

public interface SelectStatementContainer
        extends SelectAllStatement.SelectAllStatementContainer, SelectAvgStatement.SelectAvgStatementContainer,
        SelectCountStatement.SelectCountStatementContainer, SelectDistinctStatement.SelectDistinctStatementContainer,
        SelectMaxStatement.SelectMaxStatementContainer, SelectMinStatement.SelectMinStatementContainer,
        SelectStatement.SelectStatementContainer,
        SelectSumStatement.SelectSumStatementContainer, SelectTopStatement.SelectTopStatementContainer
{
}
