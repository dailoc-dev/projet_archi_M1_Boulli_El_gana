FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY target/registry-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]