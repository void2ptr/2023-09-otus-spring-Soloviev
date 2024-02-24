package ru.otus.hw;

import com.github.cloudyrock.spring.v5.EnableMongock;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SuppressWarnings("unused")
@EnableMongock
@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
