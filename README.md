# Recipe API setup and documentation

### Architecture and Design

Recipe API is designed and implemented in a multi-layer manner as a microservice to be able to
interact with other microservices via Http REST APIs. This API is implemented using 
[Spring framework](https://spring.io/) to provide *Dependency Injection* to decouple different
layers from each other as well as enjoying built-in high quality libraries like:

- [Spring Boot](https://spring.io/projects/spring-boot) as Convention Over Configuration
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)  for Object Relationship Mapping
- [Spring Security](https://spring.io/projects/spring-security) for securing the web services and endpoints
- [Spring MVC](https://spring.io/guides/gs/serving-web-content) for exposing REST endpoints

Currently, layers of the application are:

- Controller layer: controller layer is responsible for exposing service layer to the outer world via HTTP protocol.
- Service layer: service layer handles business logic and transfers object to/from controller and repository layer.
- Repository layer: repository layer is acts as an abstraction over data storage which employs ORM to facilitate not object-oriented world to programmers.
- Security (non-functional layer): JWT (JSON Web Token) is used to secure REST endpoints in a stateless manner.

### Swagger API documentation

In order to provide documentation and easy access to Recipe APIs [Swagger](https://swagger.io) is employed.
To check and call services via Swagger you should check [http://localhost:8083/abn/swagger-ui/](http://localhost:8083/abn/swagger-ui/)

### Requirements

- [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [MariaDB](https://mariadb.org/)
- [Apache Maven](https://maven.apache.org/)

### Database for Production profile

If profile is set to production (`spring.profiles.active=prod` in `application.properties`) connection string and database schemas must be set in the `application-prod.properties` file. 
By default, connection string is:
``
spring.datasource.url=jdbc:mysql://localhost:3306/recipe
``

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

### Sample requests and responses via cURL

Authentication and obtaining JWT token:
Request:
```bazaar
curl -X POST http://localhost:8083/abn/auth/signin -H "Content-Type:application/json" -d "{\"username\":\"user\", \"password\":\"@#:OJFL:OI:#J@#@#:IJ#@#OJ#\"}"
```
Response:
```bazaar
{"username":"user","token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY1NzM5MTEzMiwiZXhwIjoxNjU3Mzk0NzMyfQ.diwGn3qyip7wh_n39zASauh1qvyvGMX-ME0e145Z0pk"}
```

To call POST /recipe endpoint we need to add this JWT token to the header:
```bazaar
curl -i -XPOST -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY1NzM5MTEzMiwiZXhwIjoxNjU3Mzk0NzMyfQ.diwGn3qyip7wh_n39zASauh1qvyvGMX-ME0e145Z0pk" -H "content-type: application/json" "localhost:8083/abn/api/recipe" -d '{"id":2, "name": "sabzi Polo", "ingredients":"Sabzi", "instructions": "dam koni", "type": "REGULAR", "servingNumber": 70}'
```

respone:
```bazaar
HTTP/1.1 201 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 Jul 2022 18:44:58 GMT

{"id":2,"name":"sabzi Polo","instructions":"dam koni","type":"REGULAR","servingNumber":70,"ingredients":"Sabzi"}
```

To fetch a recipe based on its id:
```bazaar
curl -i -XGET -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY1NzM5MTEzMiwiZXhwIjoxNjU3Mzk0NzMyfQ.diwGn3qyip7wh_n39zASauh1qvyvGMX-ME0e145Z0pk" "localhost:8083/abn/api/recipe/1"
```
In the case of non-existence of such an id:
```bazaar
HTTP/1.1 404 
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-Frame-Options: DENY
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 Jul 2022 18:48:09 GMT

{"exceptionMessage":"The recipe you requested doesn't exist.","time":"2022-07-09T23:18:09.029606"}
```

To find out about complete list of endpoints please check:
```bazaar
http://localhost:8083/abn/swagger-ui
```

### Future Enhancements
- Normalization in data model
- Identity and key management like KeyCloak
- Completing unit and integration test to cover all corner cases
- Adding API Gateway in front of Recipe API
- define nice DSL language for dynamic searches 
- Exception handling
