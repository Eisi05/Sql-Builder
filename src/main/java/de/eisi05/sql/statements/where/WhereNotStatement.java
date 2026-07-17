package de.eisi05.sql.statements.where;

/**
 * Represents a marker or modifier contract for SQL conditions that incorporate a logical negation <b>{@code NOT}</b> .
 */
public interface WhereNotStatement
{
    /**
     * Indicates whether the <b>{@code NOT}</b> keyword should be placed immediately after the <b>{@code WHERE}</b> keyword.
     *
     * @return {@code true} if <b>{@code NOT}</b> follows right after <b>{@code WHERE}</b> , {@code false} otherwise
     */
    boolean isNotAfterWhere();
}