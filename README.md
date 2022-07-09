# Recipe API setup and documentation

###Architecture and Design

Recipe API is designed and implemented in a multi-layer manner as a microservice to be able to
interact with other microservices via Http REST APIs. This API is implemented using 
[Spring framework](https://spring.io/) to provide *Dependency Injection* to decouple different
layers from each other as well as enjoying built-in high quality libraries like:

- [Spring Boot](https://spring.io/projects/spring-boot) as Convention Over Configuration
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)  for Object Relationship Mapping
- [Spring Security](https://spring.io/projects/spring-security) for securing the web services and endpoints
- [Spring MVC](https://spring.io/guides/gs/serving-web-content) for exposing REST endpoints 

### Swagger API documentation

In order to provide documentation and easy access to Recipe APIs [Swagger](https://swagger.io) is employed.
To check and call services via Swagger you should check [Recipe API Swagger](http://localhost:8083/abn/swagger-ui/)

### Requirements

- [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [MariaDB](https://mariadb.org/)
- [Apache Maven](https://maven.apache.org/)

### Build and Run

#### Docker

To build the docker image in the root directory of the project the following command 
should be executed:
```bazaar
sudo docker build -t recipe-api .
```
which creates `recipe-api:latest` docker image.
To run the image run:
```bazaar
sudo docker run -p 8083:8083  recipe-api:latest
```

#### Bare metal
In order to compile and create a runnable artifact run the following command:
```mvn clean package -DskipTests```

Created jar file can be run with the following command:
```java -jar -Dspring.profiles.active=prod target/recipe-api-1.0.0.jar```

Since our project is using two profiles (one for production and one for development), we need to specify
the profile which we are going to use. *Production* profile can be activated by setting `-Dspring.profiles.active=prod` 
while running the jar file.
In the case of production profile we need to set MariaDB configs in the `application-prod.properties` file. 


