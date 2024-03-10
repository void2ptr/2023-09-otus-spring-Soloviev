FROM openjdk:17-alpine

LABEL app=spring-study-web-client

COPY /target/*.jar /app/app.jar

# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /tmp/java-spring-study-feign.log \
 && ln -sf /dev/stderr /tmp/java-spring-study-feign.log

EXPOSE 8981
EXPOSE 8091
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+PrintFlagsFinal", "-XX:+PrintGCDetails", "-jar", "/app/app.jar"]
