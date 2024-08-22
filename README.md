# SQL Builder

This repository simplifies working with SQL.

All information are based on [W3Schools SQL documentation](https://www.w3schools.com/sql/default.asp).

## Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Eisi05</groupId>
    <artifactId>Sql-Builder</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Gradle
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.Eisi05:Sql-Builder:1.0.0'
}
```

## Creating a Database and Connecting to It
### Available Database Types:
- MySql
-  Sql Server
- Oracle
- MsAccess

```java
// Example with MySql
// Parameters: host, port, database, user, password
Database database = new MySqlDatabase("localhost", 3306, "sql_builder", "root", "root");

// Connecting
Database.SQLData data = database.connect();
```

## Creating Statements
```java
// Simple selection
// This returns: SELECT * FROM TestTable WHERE ID = 5 AND NAME = 'TEST';
FinalStatement selectStatement = data.select("*").from("TestTable").where("ID").equal(5).and("NAME").equal("TEST");

// Creating a new table
// This returns: CREATE TABLE TestTable (Name VARCHAR(255) NOT NULL, ID BIGINT PRIMARY KEY);
FinalStatement createStatement = data.createTable("TestTable", 
        new TableColumn("ID", SqlDataType.BIGINT).primaryKey(),
        new TableColumn("Name", SqlDataType.VARCHAR(255)).notNull());

// Appending another statement
FinalStatement finalStatement = createStatement.appendStatement(
        data.insertInto("TestTable", new InsertIntoStatement.InsertObject("ID", 1)));

finalStatement.getQuery();
// This will return:
// CREATE TABLE TestTable (Name VARCHAR(255) NOT NULL, ID BIGINT PRIMARY KEY);
// INSERT INTO TestTable (ID) VALUES (1);
```

## Executing a Statement
Only valid statements (FinalStatements) can be executed.

1. **Execute**
```java
FinalStatement statement = ...;

// ExceptionResult is similar to Optional, but also contains an Exception if an error occurs.
ExceptionResult<Boolean> result = statement.execute();

// true if the first result is a ResultSet object; false if it is an update count or there are no results
boolean success = result.result();
```

2. **Execute Update/Large Update**
```java
FinalStatement statement = ...;

ExceptionResult<Integer> result = statement.execute();

// Returns either (1) the row count for SQL DML statements or (2) 0 for SQL statements that return nothing
int rowCount = result.result();

// For LargeUpdate, instead of an int, it returns a long
```

3. **Execute Query**
```java
FinalStatement statement = ...;

ExceptionResult<QueryResult> result = statement.execute();

// A QueryResult works similar to a ResultSet
QueryResult queryResult = result.result();

// This can also be used with a SqlDataType (only works if only one column is selected!)
ExceptionResult<Long> result = statement.execute(SqlDataType.BIGINT);
long queryResult = result.result();
```
   3.1. **getObjects(<datatype>)**
   ```java
   // Returns a list of all the objects cast to the correct Java type
   List<Long> ids = queryResult.getObjects(SqlDataType.BIGINT);
   ```
   3.2. **get(<key>)/get(<column>)**
   ```java
   // Returns a plain Object of that key/column
   long id1 = queryResult.get(1);
   long id2 = queryResult.get("ID");

   // This method can also be used with a SqlDataType
   long id1 = queryResult.get(1, SqlDataType.BIGINT);
   long id2 = queryResult.get("ID", SqlDataType.BIGINT);
   ```

4. **Custom Statement**
```java
FinalStatement statement = ...;

// This method takes a BiConsumer<Statement, String>, where Statement is a java.sql.Statement and String is the query,
// which can also be obtained with statement.getQuery();
statement.createStatement((stmt, query) -> {
    stmt.executeUpdate(query);
});
```
