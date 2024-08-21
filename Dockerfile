FROM openjdk:21-jre-slim
WORKDIR /app
COPY ./target/spring-boot-base.jar .
CMD ["java", "-jar", "spring-boot-base.jar"]