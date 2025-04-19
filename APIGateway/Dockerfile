# Use a base image with Java 21
FROM eclipse-temurin:21-jdk-jammy AS base

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
# Replace 'eureka-server.jar' with your actual JAR file name
COPY target/api-gateway-0.0.1.jar /app/api-gateway.jar

# Expose the Eureka server port (default is 8761)
EXPOSE 8080

# Run the Eureka server
ENTRYPOINT ["java", "-jar", "/app/api-gateway.jar"]