FROM openjdk:11
EXPOSE 8083
ADD target/recipe-api-1.0.0.jar recipe-api-1.0.0.jar
ENTRYPOINT ["java","-Dserver.port=8083","-jar","/recipe-api-1.0.0.jar"]