FROM openjdk:11-jre-slim-sid

LABEL maintainer="Brainweb"
LABEL version="1.0"


EXPOSE 8080

COPY target/*.jar gubee-interview.jar

ENTRYPOINT [ "java" , "-Xms128m",  "-Xmx400m", "-jar", "-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector", "gubee-interview.jar", "--jdbc.url=jdbc:postgresql://localhost:5432/postgres", "--jdbc.password=gubee", "--jdbc.username=gubee", "--jdbc.schema=interview_service"]