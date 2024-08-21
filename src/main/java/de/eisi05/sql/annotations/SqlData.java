package de.eisi05.sql.annotations;

import de.eisi05.sql.enums.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SqlData
{
    DatabaseType[] value();

    double oracleVersion() default -1;
}
