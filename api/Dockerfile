FROM openjdk:8-jdk-alpine
ARG MYSQL_HOST
ARG MYSQL_PORT
ARG MYSQL_DATABASE
ENV MYSQL_URL "jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false&allowMultiQueries=true"
ARG MYSQL_PASSWORD
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=${MYSQL_URL}", "-Dspring.datasource.password=${MYSQL_PASSWORD}", "/app.jar"]