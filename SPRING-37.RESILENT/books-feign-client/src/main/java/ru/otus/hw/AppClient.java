package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AppClient {

	public static void main(String[] args) {
		SpringApplication.run(AppClient.class, args);
//		System.out.printf("Prometheus: metrics to: %s%n", "http://localhost:8091/actuator/prometheus");
		System.out.printf("swagger: ui        to: %s%n", "http://localhost:8981/swagger-ui/index.html");
		System.out.printf("swagger: OpenApi   to: %s%n", "http://localhost:8981/v3/api-docs");
	}

}
