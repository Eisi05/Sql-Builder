package de.eisi05.sql.result;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A container object which may or may not contain a non-null database execution result. If an exception occurred during execution, it captures the thrown
 * {@link RuntimeException}.
 *
 * @param <T> the type of the execution result value
 */
public record ExecutionResult<T>(T result, RuntimeException exception)
{
    /**
     * Returns an {@code ExecutionResult} describing the given non-null value.
     *
     * @param result the result value to describe
     * @param <T>    the type of the value
     * @return an {@code ExecutionResult} with the value present
     */
    public static <T> ExecutionResult<T> of(T result)
    {
        return new ExecutionResult<>(result, null);
    }

    /**
     * Returns an {@code ExecutionResult} containing the specified exception.
     *
     * @param exception the runtime exception that occurred during execution
     * @param <T>       the type of the expected value
     * @return an {@code ExecutionResult} with the exception present
     */
    public static <T> ExecutionResult<T> ofException(RuntimeException exception)
    {
        return new ExecutionResult<>(null, exception);
    }

    /**
     * Returns an empty {@code ExecutionResult} instance with no value or exception.
     *
     * @param <T> the type of the expected value
     * @return an empty {@code ExecutionResult}
     */
    public static <T> ExecutionResult<T> empty()
    {
        return new ExecutionResult<>(null, null);
    }

    /**
     * If a value is present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is present, otherwise {@code false}
     */
    public boolean isPresent()
    {
        return result != null;
    }

    /**
     * If a value is not present, returns {@code true}, otherwise {@code false}.
     *
     * @return {@code true} if a value is not present, otherwise {@code false}
     */
    public boolean isEmpty()
    {
        return result == null;
    }

    /**
     * If a value is present, performs the given action with the value, otherwise does nothing.
     *
     * @param action the action to be performed if a value is present
     */
    public void ifPresent(Consumer<? super T> action)
    {
        if(result != null)
            action.accept(result);
    }

    /**
     * If a value is present, performs the given action with the value, otherwise performs the given empty-based action.
     *
     * @param action      the action to be performed if a value is present
     * @param emptyAction the empty-based action to be performed if no value is present
     */
    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        if(result != null)
            action.accept(result);
        else
            emptyAction.run();
    }

    /**
     * If a value is present, and the value matches the given predicate, returns an {@code ExecutionResult} describing the value, otherwise returns an empty
     * {@code ExecutionResult}.
     *
     * @param predicate the predicate to apply to a value, if present
     * @return an {@code ExecutionResult} describing the value of this {@code ExecutionResult}, if a value is present and matches the predicate, otherwise an
     * empty {@code ExecutionResult}
     */
    public ExecutionResult<T> filter(Predicate<? super T> predicate)
    {
        Objects.requireNonNull(predicate);
        if(!isPresent())
            return this;
        else
            return predicate.test(result) ? this : empty();
    }

    /**
     * If a value is present, returns an {@code ExecutionResult} describing the result of applying the given mapping function to the value, otherwise returns an
     * empty {@code ExecutionResult}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U>    The type of the value returned from the mapping function
     * @return an {@code ExecutionResult} describing the result of applying a mapping function to the value of this {@code ExecutionResult}, if a value is
     * present, otherwise an empty {@code ExecutionResult}
     */
    public <U> ExecutionResult<U> map(Function<? super T, ? extends U> mapper)
    {
        Objects.requireNonNull(mapper);
        if(!isPresent())
            return empty();
        else
            return new ExecutionResult<>(mapper.apply(result), exception);
    }

    /**
     * If the underlying result is a list, maps all containing {@link QueryResult} instances to a stream of the targeted class type.
     *
     * @param clazz the target mapped class
     * @param <U>   the target mapping component type
     * @return a stream of mapped objects, or an empty stream if mapping is not applicable
     */
    public <U> Stream<U> mapAllTo(Class<U> clazz)
    {
        if(result == null || !(result instanceof List<?> queryResults))
            return Stream.empty();

        return queryResults.stream()
                .filter(o -> o instanceof QueryResult)
                .map(o -> ((QueryResult) o).map(clazz))
                .filter(Objects::nonNull);
    }

    /**
     * If a value is present, returns the result of applying the given {@code ExecutionResult}-bearing mapping function to the value, otherwise returns an empty
     * {@code ExecutionResult}.
     *
     * @param mapper the mapping function to apply to a value, if present
     * @param <U>    the type of value of the {@code ExecutionResult} returned by the mapping function
     * @return the result of applying an {@code ExecutionResult}-bearing mapping function to the value of this {@code ExecutionResult}, if a value is present,
     * otherwise an empty {@code ExecutionResult}
     */
    public <U> ExecutionResult<U> flatMap(Function<? super T, ? extends ExecutionResult<? extends U>> mapper)
    {
        Objects.requireNonNull(mapper);
        if(!isPresent())
            return empty();
        else
        {
            @SuppressWarnings("unchecked")
            ExecutionResult<U> r = (ExecutionResult<U>) mapper.apply(result);
            return Objects.requireNonNull(r);
        }
    }

    /**
     * If a value is present, returns an {@code ExecutionResult} describing the value, otherwise returns an {@code ExecutionResult} produced by the supplying
     * function.
     *
     * @param supplier the supplying function that produces an alternative {@code ExecutionResult}
     * @return returns an {@code ExecutionResult} describing the value of this {@code ExecutionResult}, if a value is present, otherwise an
     * {@code ExecutionResult} produced by the supplying function
     */
    public ExecutionResult<T> or(Supplier<? extends ExecutionResult<? extends T>> supplier)
    {
        Objects.requireNonNull(supplier);
        if(isPresent())
            return this;
        else
        {
            @SuppressWarnings("unchecked")
            ExecutionResult<T> r = (ExecutionResult<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    /**
     * If a value is present, returns a sequential {@link Stream} containing only that value, otherwise returns an empty {@code Stream}.
     *
     * @return the optional value as a {@code Stream}
     */
    public Stream<T> stream()
    {
        if(!isPresent())
            return Stream.empty();
        else
            return Stream.of(result);
    }

    /**
     * If a value is present, returns the value, otherwise returns {@code other}.
     *
     * @param other the value to be returned if no value is present
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other)
    {
        return result != null ? result : other;
    }

    /**
     * If a value is present, returns the value, otherwise returns the result produced by the supplying function.
     *
     * @param supplier the supplying function that produces a value to be returned
     * @return the value, if present, otherwise the result produced by the supplying function
     */
    public T orElseGet(Supplier<? extends T> supplier)
    {
        return result != null ? result : supplier.get();
    }

    /**
     * If a value is present, returns the value, otherwise throws the internal captured exception, or a default {@link RuntimeException} if no exception was
     * explicitly recorded.
     *
     * @return the non-null value held by this {@code ExecutionResult}
     * @throws RuntimeException if no value is present
     */
    public T orElseThrow()
    {
        if(result == null)
            throw (exception == null ? new RuntimeException() : exception);
        return result;
    }

    /**
     * If a value is present, returns the value, otherwise throws an exception produced by the exception supplying function.
     *
     * @param exceptionSupplier the supplying function which provides the exception to be thrown
     * @param <X>               Type of the exception to be thrown
     * @return the value, if present
     * @throws X if no value is present
     */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
    {
        if(result != null)
            return result;
        else
            throw exceptionSupplier.get();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        return obj instanceof ExecutionResult<?> other && Objects.equals(result, other.result);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(result);
    }

    /**
     * Explicit getter override enforcing that a value must be present, throwing an exception if not.
     *
     * @return the execution result value
     * @throws NoSuchElementException if no value is present
     */
    @Override
    public T result()
    {
        if(result == null)
            throw new NoSuchElementException("No value present");

        return result;
    }

    /**
     * Explicit getter override enforcing that an exception must be present, throwing an exception if not.
     *
     * @return the internal runtime exception context
     * @throws NoSuchElementException if no exception is present
     */
    @Override
    public RuntimeException exception()
    {
        if(exception == null)
            throw new NoSuchElementException("No exception present");

        return exception;
    }

    /**
     * Checks if a captured runtime exception is present.
     *
     * @return {@code true} if an exception was caught, otherwise {@code false}
     */
    public boolean hasException()
    {
        return exception != null;
    }
}