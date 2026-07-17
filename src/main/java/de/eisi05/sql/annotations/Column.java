package de.eisi05.sql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated field represents a column in a database table. This annotation configures the mapping details of the database column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column
{
    /**
     * The name of the column in the database. If left empty, defaults to the name of the annotated field.
     *
     * @return the name of the column
     */
    String name() default "";

    /**
     * The immediate previous name of this column in the database. Used by the migration engine to execute safe structural
     * <b>{@code ALTER TABLE ... RENAME COLUMN}</b> commands rather than fallback drop-and-add sequences.
     *
     * @return the previous name of the column, or an empty string if it has not been renamed
     */
    String oldName() default "";

    /**
     * Specifies whether the database column can be null.
     *
     * @return true if the column cannot be null, false otherwise
     */
    boolean notNull() default false;

    /**
     * Specifies whether the database column has a unique constraint.
     *
     * @return true if the values in this column must be unique, false otherwise
     */
    boolean unique() default false;

    /**
     * The default value for the column, defined as a String.
     *
     * @return the default value expression for the column
     */
    String defaultValue() default "";

    /**
     * The SQL fragment that is used when generating the DDL for the column. Can be used to explicitly define database-specific types (e.g., "VARCHAR(255)" or
     * "JSONB").
     *
     * @return the custom column definition SQL
     */
    String columnDefinition() default "";
}