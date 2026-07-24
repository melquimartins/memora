FROM maven:3-eclipse-temurin-26 AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package

FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/memora-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
