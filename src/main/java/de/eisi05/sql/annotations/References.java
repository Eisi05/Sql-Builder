package de.eisi05.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Establishes a foreign key relationship, indicating that the annotated field references another table.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface References
{
    /**
     * The target entity class that this foreign key references.
     *
     * @return the referenced entity class
     */
    Class<?> value();

    /**
     * The name of the column in the target table being referenced. Defaults to "id".
     *
     * @return the name of the referenced column
     */
    String column() default "id";
}