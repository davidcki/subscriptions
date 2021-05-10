FROM openjdk:12-jdk-alpine
WORKDIR /opt/apps/
COPY build/libs/public-api-1.0.jar public-api-1.0.jar
ENTRYPOINT ["java", "-jar", "public-api-1.0.jar"]
