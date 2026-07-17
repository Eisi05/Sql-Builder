package de.eisi05.sql.statements.select;

/**
 * Unified compound interface containing all variations of SQL SELECT statement builder containers.
 */
public interface SelectStatementContainer
        extends SelectAllStatement.SelectAllStatementContainer, SelectAvgStatement.SelectAvgStatementContainer,
                SelectCountStatement.SelectCountStatementContainer, SelectDistinctStatement.SelectDistinctStatementContainer,
                SelectMaxStatement.SelectMaxStatementContainer, SelectMinStatement.SelectMinStatementContainer,
                SelectStatement.SelectStatementContainer,
                SelectSumStatement.SelectSumStatementContainer, SelectTopStatement.SelectTopStatementContainer
{
}
