FROM openjdk:17-jdk-alpine

EXPOSE 8080

ARG JAR_FILE=build/libs/qrcode-service-*.jar

ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]