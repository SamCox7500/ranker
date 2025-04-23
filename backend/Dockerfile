#Use a base image with Java installed
FROM maven:3.9.6-eclipse-temurin-17 AS builder

#Set the working directory in the container
WORKDIR /app

#Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

#Copy the entire backend folder to the container
COPY src ./src

#Package the Spring Boot app
RUN mvn clean package -DskipTests

#Use a smaller runtime image to run the Spring Boot app
FROM openjdk:17-slim

#Copy the packaged jar from the builder image
COPY --from=builder /app/target/*.jar app.jar

#Expose the port the app runs on
EXPOSE 8080

#Command to run the app
ENTRYPOINT ["java","-jar","/app.jar"]
