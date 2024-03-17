FROM openjdk:17-alpine

LABEL app=spring-study-circuit-breaker

COPY /target/*.jar /app/app.jar

# forward request and error logs to docker log collector
RUN ln -sf /dev/stdout /tmp/spring-study-circuit-breaker.log \
 && ln -sf /dev/stderr /tmp/spring-study-circuit-breaker.log

EXPOSE 8080
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
