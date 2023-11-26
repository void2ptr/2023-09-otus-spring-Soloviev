package ru.otus.hw.comfig;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.hw.*")
public class AppConfig {

//    @Bean
//    public Mongo mongo() throws Exception {
//        return new Mongo("localhost");
//    }
//
//    public String getDatabaseName() {
//        return "otus";
//    }
//
//    public String getMappingBasePackage() {
//        return "ru.otus.hw";
//    }
}
