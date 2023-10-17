package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationProps.class)
public class ApplicationConfig {

    @Bean
    public ApplicationMessage applicationConfig(@Value("${application.message}") String message){
        return new ApplicationMessage(message);
    }
}
