FROM openjdk:15-jdk-slim

WORKDIR application

COPY /target/*.jar app.jar

RUN addgroup demogroup && adduser  --ingroup demogroup --disabled-password demo
USER demo

ENTRYPOINT ["java","-jar","app.jar"]