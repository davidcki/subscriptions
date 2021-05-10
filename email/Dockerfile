FROM openjdk:12-jdk-alpine
WORKDIR /opt/apps/
COPY build/libs/email-1.0.jar email-1.0.jar
ENTRYPOINT ["java", "-jar", "email-1.0.jar"]
