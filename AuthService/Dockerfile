FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY AuthService/pom.xml ./

COPY AuthService/src ./src
RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/AuthService-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/app.jar"]