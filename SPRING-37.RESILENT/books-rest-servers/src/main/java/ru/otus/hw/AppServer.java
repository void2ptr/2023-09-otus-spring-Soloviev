package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppServer {

	public static void main(String[] args) {
		SpringApplication.run(AppServer.class, args);
		System.out.printf("Server listen       on: %s%n", "http://localhost:8080");
		System.out.printf("Actuator root       to: %s%n", "http://localhost:8090/actuator");
		System.out.printf("Actuator Health     to: %s%n", "http://localhost:8090/actuator/health");
		System.out.printf("Actuator Logfile    to: %s%n", "http://localhost:8090/actuator/logfile");
		System.out.printf("Prometheus: metrics to: %s%n", "http://localhost:8090/actuator/prometheus");
		System.out.printf("Prometheus: GUI     to: %s%n", "http://localhost:9095/graph");
		System.out.printf("HATEOAS             to: %s%n", "http://localhost:8080/hateoas/api");
		System.out.printf("Grafana             to: %s%n", "http://localhost:3000");
		System.out.printf("swagger: ui         to: %s%n", "http://localhost:8080/swagger-ui/index.html");
		System.out.printf("swagger: api-docs   to: %s%n", "http://localhost:8080/v3/api-docs");
	}

}
