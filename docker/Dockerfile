FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN mvn -B -q dependency:go-offline

COPY src src

RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

RUN useradd -r -u 1001 appuser

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
