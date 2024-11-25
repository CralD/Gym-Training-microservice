FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./training-service/target/training-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]