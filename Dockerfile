FROM maven:3.9.6-eclipse-temurin-21-alpine AS deps
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM deps AS builder
COPY . .
RUN mvn clean package -B -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/search-engines.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]