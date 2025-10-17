FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src src

RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

EXPOSE 7777

COPY --from=builder /app/target/banking-app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
