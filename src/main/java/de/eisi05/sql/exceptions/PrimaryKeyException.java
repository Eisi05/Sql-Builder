package de.eisi05.sql.exceptions;

public class PrimaryKeyException extends RuntimeException
{
    public PrimaryKeyException(String message)
    {
        super(message);
    }
}
