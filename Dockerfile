# Use The Official Maven Image In Docker To Build This DockerFile
FROM maven:3.8.4-openjdk-17 AS build

# Set The Working Directory
WORKDIR /app

# Copy The Pom.xml And Install Dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy The Source Code And Build The Application
COPY src ./src
RUN mvn clean package -DskipTests

# Use An Official OpenJDK Image To Run The Application
FROM openjdk:17-jdk-slim

# Set The Working Directory
WORKDIR /app

# Copy The Built JAR File From The Build Stage (Fixed)
COPY --from=build /app/target/springboot-ecommerceProject-0.0.1-SNAPSHOT.jar /app/springboot-ecommerceProject-0.0.1-SNAPSHOT.jar

# Expose The Port 8080
EXPOSE 8080

# Specify The Command To Run The Application
ENTRYPOINT ["java", "-jar", "/app/springboot-ecommerceProject-0.0.1-SNAPSHOT.jar"]
