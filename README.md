# Document management REST service

Test project. It has three REST end-points to add file content (left and right parts of a document separately) 
and compare it.

## Getting Started

To start this application on your local machine you can:
  - run it from IDE (IntelliJIdea for example) as spring-boot project
  - build it as jar
   (using maven as it show below. It will also run tests)
   
   ```
   ./mvnw clean package
   ```
   and then run
   
   ```
   java -jar differentiator-0.0.1-SNAPSHOT.jar
   ```
  - run using maven
  
   ```   
  ./gradlew bootRun
   ```
### Prerequisites

It's better to have maven installed. Also you should have Java on your machine.

### Installing

Please, see 'Getting Started' section

## Running the tests

Tests (Unit and Integration) are executed during the start of the application.

## Deployment

To build a war file that is both executable and deployable into an external container you need to mark the embedded container dependencies as “provided”, e.g:

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <!-- ... -->
    <packaging>war</packaging>
    <!-- ... -->
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
        <!-- ... -->
    </dependencies>
</project>
```

## Using

There are three end-points:

Two Post requests with Content-Type=application/json
to store the data (content) 
for the document with specified <ID>
with Json body. Content of the request is validated to meet the requirement for base64 encoded data.

```
<host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
 ```


Example of request (Response with http code 200):

```
http://localhost:8080/v1/diff/1/left

{
	"content":"exampleContent"
}
 
```

Invalid request:
```
http://localhost:8080/v1/diff/1/left

{
	"content":"example Content"
}
 
```


One Get request to get the differences between left and right part of the document with specified <ID>

```
<host>/v1/diff/<ID>
```
Example of request:
```
http://localhost:8080/v1/diff/1

 
```

If there is no document with this ID you will receive response with http code 404 (Not Found) and description
```
{
    "error_message": "There is no document with id: 1"
}
```

If document's left and right parts are of the different size:
```
{
    "status": "DIFFERENT_SIZE"
}
```

If document's left and right parts have the same content:
```
{
    "status": "EQUAL"
}
```

If document's left and right parts are of the same size but of the different content you will recieve:

```
{
    "status": "DIFFERENT_CONTENT",
    "diffs" : [{"offset":0, "size":2}...]
}
```




## Built With

* [Java](https://www.oracle.com/java/index.html) - Programming Language
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring](https://spring.io/) - Framework with various of modules to build EE applications
* [Spring-boot](http://projects.spring.io/spring-boot/) - The Easiest way to start application with Spring Framework
* [H2 DB](http://www.h2database.com/html/main.html) - Embedded DB

## Authors

* **Vadzim Mikhalenak**

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
