#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#COPY . .
#RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-alpine
VOLUME /tmp
#COPY --from=build /app/target/*.jar app.jar
#COPY /app/target/worker-1.0-SNAPSHOT.jar app.jar
COPY target/worker-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]