package de.eisi05.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the value of the annotated field will be automatically generated
 * by the database (typically used in conjunction with {@link Id}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GeneratedValue
{
    /**
     * Indicates whether the database should automatically increment the value
     * for this column on new record insertions.
     *
     * @return true if auto-increment is enabled, false otherwise
     */
    boolean autoIncrement() default true;
}