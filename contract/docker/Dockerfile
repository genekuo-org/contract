FROM openjdk:alpine

MAINTAINER Gene Kuo

ENV APP_HOME /app/

COPY target/contract-0.0.1-SNAPSHOT.jar $APP_HOME

EXPOSE 8080

WORKDIR $APP_HOME

# set env variables when starting the container
CMD java -jar contract-0.0.1-SNAPSHOT.jar