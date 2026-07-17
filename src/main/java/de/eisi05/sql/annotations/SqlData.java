package de.eisi05.sql.annotations;

import de.eisi05.sql.enums.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines SQL-specific configuration parameters for an entity, including targeted database systems and database-specific versions.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlData
{
    /**
     * The array of supported database types for this entity.
     *
     * @return the supported database types
     */
    DatabaseType[] value();

    /**
     * Specifies the minimum or specific Oracle database version required, if applicable. Defaults to -1 indicating that Oracle versioning is not specified.
     *
     * @return the required Oracle version
     */
    double oracleVersion() default -1;
}
