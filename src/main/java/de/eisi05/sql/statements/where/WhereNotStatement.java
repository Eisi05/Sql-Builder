package de.eisi05.sql.statements.where;

/**
 * Represents a marker or modifier contract for SQL conditions that incorporate a logical negation {@code NOT}.
 */
public interface WhereNotStatement
{
    /**
     * Indicates whether the {@code NOT} keyword should be placed immediately after the {@code WHERE} keyword.
     *
     * @return {@code true} if {@code NOT} follows right after {@code WHERE}, {@code false} otherwise
     */
    boolean isNotAfterWhere();
}