FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./eureka-server/target/eureka-server-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]