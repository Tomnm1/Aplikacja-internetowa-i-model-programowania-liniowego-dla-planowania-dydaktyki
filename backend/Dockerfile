FROM maven:3.9.9-eclipse-temurin-21-alpine as build-stage

WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build-stage /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
