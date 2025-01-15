## Step 1: Use a lightweight JDK image as the base image
FROM openjdk:17-jdk-slim as build

# Step 2: Set working directory inside the container
WORKDIR /app

# Step 3: Copy the Gradle wrapper and build files to the container
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts /app/
COPY gradle /app/gradle

# Step 4: Download dependencies
RUN ./gradlew dependencies

# Step 5: Copy the source code
COPY src /app/src

# Step 6: Build the application
RUN ./gradlew build -x test

# Step 7: Use a lightweight JDK image to run the application
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your Ktor application runs on (default: 8080)
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "app.jar"]
