# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8092

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]

