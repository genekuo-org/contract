FROM maven:3.6.3-jdk-11 AS builder

WORKDIR /usr/src/api
COPY ../pom.xml .
RUN mvn -B dependency:go-offline

COPY . .
RUN mvn package

# app
FROM openjdk:11.0.11-jre-slim-buster

WORKDIR /app
COPY --from=builder /usr/src/api/target/contract-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/contract-0.0.1-SNAPSHOT.jar"]

ENV JRE_VERSION="11.0.11" \
    APP_VERSION="0.0.1"