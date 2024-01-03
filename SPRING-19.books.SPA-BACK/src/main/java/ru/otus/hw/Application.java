package ru.otus.hw;

//import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.printf("Server listen on: %n%s%n", "http://localhost:8080");

//		Console.main(args);
	}

}
