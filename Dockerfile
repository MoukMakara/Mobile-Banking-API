# Stage 1: Build the JAR
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Stage 2: Run the JAR
FROM ghcr.io/graalvm/jdk-community:21
WORKDIR /app
COPY --from=builder /app/build/libs/mbanking-api-1.0.jar /app/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/mbanking-api-1.0.jar"]
