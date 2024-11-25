FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./gym-service/target/gym-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]