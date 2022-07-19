FROM openjdk:11
COPY target/spring-boot-docker.jar spring-boot-docker.jar
EXPOSE 8082
CMD java -jar spring-boot-docker.jar