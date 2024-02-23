package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Server listen      on: %s%n", "http://localhost:8080");
		System.out.printf("Actuator root      to: %s%n", "http://localhost:8080/actuator");
		System.out.printf("Actuator Health    to: %s%n", "http://localhost:8080/actuator/health");
		System.out.printf("Actuator Logfile   to: %s%n", "http://localhost:8080/actuator/logfile");
		System.out.printf("Prometheus metrics to: %s%n", "http://localhost:8080/actuator/prometheus");
		System.out.printf("Prometheus GUI     to: %s%n", "http://localhost:9095/graph");
		System.out.printf("HATEOS             to: %s%n", "http://localhost:8080/rest");
	}

}
