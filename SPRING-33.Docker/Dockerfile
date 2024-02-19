FROM openjdk:17-alpine

LABEL app=java-spring-study-docker

COPY /target/spring-study-docker-*.jar /app/app.jar

# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /tmp/java-spring-study-docker.log \
 && ln -sf /dev/stderr /tmp/java-spring-study-docker.log

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+PrintFlagsFinal", "-XX:+PrintGCDetails", "-jar", "/app/app.jar"]
