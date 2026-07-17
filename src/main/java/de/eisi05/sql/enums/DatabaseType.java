package de.eisi05.sql.enums;

/**
 * Defines the supported database management systems (DBMS) within the framework.
 */
public enum DatabaseType
{
    /**
     * Represents the MySQL database system.
     */
    MYSQL,

    /**
     * Represents the Microsoft SQL Server database system.
     */
    SQL_SERVER,

    /**
     * Represents the Microsoft Access database system (typically via Ucanaccess).
     */
    MS_ACCESS,

    /**
     * Represents the Oracle database system.
     */
    ORACLE,

    /**
     * Represents the PostgreSQL database system.
     */
    POSTGRESQL
}