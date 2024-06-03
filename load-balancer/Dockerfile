#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#COPY . .
#RUN mvn clean install -DskipTests

# Use Maven image to build the project
FROM openjdk:17-jdk-alpine
VOLUME /tmp
#COPY /app/target/load-balancer-1.0-SNAPSHOT.jar app.jar
COPY target/load-balancer-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]