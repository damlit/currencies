FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=target/currencies-0.0.1-SNAPSHOT.jar

ENV DB_ENDPOINT=curr-db

WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djasypt.encryptor.password=SECRET_KEY", "-jar", "app.jar"]