package de.eisi05.sql.enums;

/**
 * Defines logical operators used for combining conditions in SQL queries (e.g., in <b>{@code WHERE}</b> clauses).
 */
public enum LogicOperator
{
    /**
     * Represents the logical <b>{@code AND}</b> operator. Both conditions must evaluate to true.
     */
    AND,

    /**
     * Represents the logical <b>{@code OR}</b> operator. At least one condition must evaluate to true.
     */
    OR
}
