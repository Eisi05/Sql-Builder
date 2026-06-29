package de.eisi05.sql.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column
{
    String name() default "";

    boolean notNull() default false;

    boolean unique() default false;

    String defaultValue() default "";

    String columnDefinition() default "";
}