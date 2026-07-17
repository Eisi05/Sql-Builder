package de.eisi05.sql.exceptions;

/**
 * Thrown when a validation or integrity constraint regarding a primary key is violated. For example, this could be triggered by missing a required {@code @Id}
 * annotation or duplicate keys.
 */
public class PrimaryKeyException extends RuntimeException
{
    /**
     * Constructs a new PrimaryKeyException with the specified detail message.
     *
     * @param message the detail message explaining the primary key conflict or violation
     */
    public PrimaryKeyException(String message)
    {
        super(message);
    }
}
