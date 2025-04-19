# Use a base image with Java 21
FROM eclipse-temurin:21-jdk-jammy AS base

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
# Replace 'eureka-server.jar' with your actual JAR file name
COPY target/order-service-0.0.1.jar /app/order-service.jar

# Expose the Eureka server port (default is 8761)
EXPOSE 8081

# Run the Eureka server
ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]