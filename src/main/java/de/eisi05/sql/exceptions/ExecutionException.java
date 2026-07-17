package de.eisi05.sql.exceptions;

/**
 * Thrown when a general SQL execution error occurs while processing a statement.
 */
public class ExecutionException extends RuntimeException
{
    /**
     * Constructs a new ExecutionException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public ExecutionException(String message)
    {
        super(message);
    }
}