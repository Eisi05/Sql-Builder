package de.eisi05.sql.exceptions;

/**
 * Thrown when an error occurs specifically during a database insert operation.
 */
public class InsertionException extends RuntimeException
{
    /**
     * Constructs a new InsertionException with the specified detail message.
     *
     * @param message the detail message explaining why the insert failed
     */
    public InsertionException(String message)
    {
        super(message);
    }
}
