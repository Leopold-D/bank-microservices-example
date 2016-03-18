FROM java:8
VOLUME /tmp
ADD garage-api-microservice-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8084
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
