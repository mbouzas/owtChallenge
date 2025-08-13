# Use an OpenJDK base image
FROM eclipse-temurin:21-jdk as Builder

# Set the working directory inside the container
WORKDIR /app

# Copy appl code
COPY . .


# Package the application
RUN ./mvnw clean package

# Run the app
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=Builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]