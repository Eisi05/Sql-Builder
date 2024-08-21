package de.eisi05.sql.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ExecutionResult<T>(T result, RuntimeException exception)
{
    public static <T> ExecutionResult<T> of(T result)
    {
        return new ExecutionResult<>(result, null);
    }

    public static <T> ExecutionResult<T> ofException(RuntimeException exception)
    {
        return new ExecutionResult<>(null, exception);
    }

    public static <T> ExecutionResult<T> empty()
    {
        return new ExecutionResult<>(null, null);
    }

    public boolean isPresent()
    {
        return result != null;
    }

    public boolean isEmpty()
    {
        return result == null;
    }

    public void ifPresent(Consumer<? super T> action)
    {
        if(result != null)
            action.accept(result);
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)
    {
        if(result != null)
            action.accept(result);
        else
            emptyAction.run();
    }

    public ExecutionResult<T> filter(Predicate<? super T> predicate)
    {
        Objects.requireNonNull(predicate);
        if(!isPresent())
            return this;
        else
            return predicate.test(result) ? this : empty();
    }

    public <U> ExecutionResult<U> map(Function<? super T, ? extends U> mapper)
    {
        Objects.requireNonNull(mapper);
        if(!isPresent())
            return empty();
        else
            return new ExecutionResult<>(mapper.apply(result), exception);
    }

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

    public Stream<T> stream()
    {
        if(!isPresent())
            return Stream.empty();
        else
            return Stream.of(result);
    }

    public T orElse(T other)
    {
        return result != null ? result : other;
    }

    public T orElseGet(Supplier<? extends T> supplier)
    {
        return result != null ? result : supplier.get();
    }

    public T orElseThrow()
    {
        if(result == null)
            throw exception;
        return result;
    }

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

    @Override
    public T result()
    {
        if(result == null)
            throw new NoSuchElementException("No value present");

        return result;
    }

    @Override
    public RuntimeException exception()
    {
        if(exception == null)
            throw new NoSuchElementException("No exception present");

        return exception;
    }

    public boolean hasException()
    {
        return exception != null;
    }
}
