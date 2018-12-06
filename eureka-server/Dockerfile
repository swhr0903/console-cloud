FROM java:8
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8011
ENTRYPOINT ["java","-Ddruid.config.decrypt=true -Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]