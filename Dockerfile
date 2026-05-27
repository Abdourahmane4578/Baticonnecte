# Étape 1 : Build
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Port Spring Boot
EXPOSE 8090

# Lancement
ENTRYPOINT ["java", "-jar", "app.jar"]