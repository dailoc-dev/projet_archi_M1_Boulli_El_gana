# Stage 1: Build worker jar
FROM maven:3.8.4-openjdk-11 as builder
WORKDIR /worker
COPY ../worker /worker
RUN mvn clean install -DskipTests

# Stage 2: Setup Docker-in-Docker
FROM docker:19.03.12-dind

RUN apk add --no-cache openjdk11-jdk maven curl

COPY --from=builder /worker/target/worker-1.0-SNAPSHOT.jar /worker/target/worker-1.0-SNAPSHOT.jar

RUN docker build -t worker-image .

# Expose Docker daemon port
EXPOSE 2375

ENTRYPOINT ["sh", "-c", "curl -X POST \"http://$registry_url/register-node?node=$node_id\""]


