[![](https://jitpack.io/v/Eisi05/Sql-Builder.svg)](https://jitpack.io/#Eisi05/Sql-Builder)

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
    <version>1.4.2</version>
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
    implementation 'com.github.Eisi05:Sql-Builder:1.4.2'
}
```

## Creating a Database and Connecting to It

### Available Database Types:

* MySql
* Sql Server
* Oracle
* MsAccess
* Postgresql

```java
// Example with MySql
// Parameters: host, port, database, user, password
Database database = new MySqlDatabase("localhost", 3306, "sql_builder", "root", "root");

// Connecting to the database to get a container session
Database.SQLData data = database.connect();
```

## Creating Statements
```java
// Simple selection
// This returns: SELECT * FROM TestTable WHERE ID = 5 AND NAME = 'TEST';
FinalStatement selectStatement = data.select("*").from("TestTable").where("ID").equal(5).and("NAME").equal("TEST");

// Creating a new table manually via TableColumn structures
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

// ExecutionResult acts similarly to an Optional, but also captures thrown Exceptions if an execution error occurs.
ExecutionResult<Boolean> result = statement.execute();

// true if the first result is a ResultSet object; false if it is an update count or there are no results
boolean success = result.result();
```

2. **Execute Update/Large Update**
```java
FinalStatement statement = ...;

ExecutionResult<Integer> result = statement.executeUpdate();

// Returns either (1) the row count for SQL DML statements or (2) 0 for SQL statements that return nothing
int rowCount = result.result();

// For LargeUpdate, instead of an int, it returns a long
ExecutionResult<Long> largeResult = statement.executeLargeUpdate();
```

3. **Execute Query**
```java
FinalStatement statement = ...;

// executeQuery() returns an ExecutionResult containing a List of QueryResult (one per row)
ExecutionResult<List<QueryResult>> result = statement.executeQuery();

// Access the list of rows directly if present
List<QueryResult> rows = result.result();
if (!rows.isEmpty()) {
    QueryResult queryResult = rows.get(0);
    
    long id = queryResult.get("ID");
}

// This can also be used with a SqlDataType directly (only works if a single column/value is selected!)
ExecutionResult<Long> singleResult = statement.executeQuery(SqlDataType.BIGINT);
long queryResultValue = singleResult.result();
```

3.1. **getObjects(`<datatype>`)**

```java
// Returns a list of all column cell values cast to the correct Java type from a single row context
List<Long> ids = queryResult.getObjects(SqlDataType.BIGINT);
```

3.2. **get(`<key>`)/get(`<column>`)**

```java
// Returns a plain Object for that key/column
long id1 = queryResult.get(1);
long id2 = queryResult.get("ID");

// This method can also be used with an explicit SqlDataType mapping
long idTyped1 = queryResult.get(1, SqlDataType.BIGINT);
long idTyped2 = queryResult.get("ID", SqlDataType.BIGINT);
```

4. **Custom Statement**

```java
FinalStatement statement = ...;

// This method takes a BiConsumer<Statement, String>, where Statement is a java.sql.Statement and String is the query string,
// which can also be explicitly requested with statement.getQuery();
statement.createStatement((stmt, query) -> {
    stmt.executeUpdate(query);
});
```
---

## Object-Relational Mapping (ORM) Entities

The framework provides mapping capabilities to declare structures directly through traditional Java objects or Records and interact via database metadata extraction routines.

### 1. Declaring Entities (POJOs or Records)

```java
import de.eisi05.sql.annotations.*;
import de.eisi05.sql.interfaces.SqlDataType;
import java.time.LocalDateTime;

// Mapping a standard table entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(autoIncrement = true)
    @Column(name = "id")
    private int id;

    @Column(name = "username", notNull = true, unique = true)
    private String username;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    // A persistence constructor strategy used for mapping routine injections
    @PersistenceConstructor
    public User(int id, String username, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }
}

// Or declare as a native Java Record!
@Table(name = "products")
public record Product(
    @Id @Column(name = "product_id") long productId,
    @Column(name = "title") String title,
    @Column(name = "metadata", columnDefinition = "JSONB") Map<String, Object> metadata
) {}
```

### 2. Using ORM Utilities to Construct Queries

You can utilize `OrmUtils` to reflectively generate mappings, fetch Primary Key identifiers, or scrub data formats dynamically:

```java
import de.eisi05.sql.utils.OrmUtils;

User user = new User(0, "eisi05", LocalDateTime.now());

// 1. Automatically resolve table name from metadata annotations ("users")
String tableName = OrmUtils.resolveTable(User.class);

// 2. Generate column-to-value map for dynamic inserts/updates (skips auto-incremented fields!)
Map<String, Object> columnValueMap = OrmUtils.toColumnMap(user);
// Result Map contains: {"username": "eisi05", "created_at": <timestamp_object>}

// 3. Extract primary identity fields reflectively
Object idValue = OrmUtils.extractId(user);
```

### 3. Mapping Query Results Back to Objects

When retrieving database listings, the query result returns rows inside an `ExecutionResult`. You can turn these row items directly into streams or object lists using `mapAllTo()`:

```java
// Retrieving a collection list of mapped items
List<Product> products = data.selectAll(Product.class)
        .executeQuery()
        .mapAllTo(Product.class)
        .toList();

// Retrieving and safely picking a single optional record out of a unique row query
Optional<User> userOptional = data.selectAll(Product.class)
        .where("id").equal(42)
        .executeQuery()
        .mapAllTo(User.class)
        .findFirst();
```
