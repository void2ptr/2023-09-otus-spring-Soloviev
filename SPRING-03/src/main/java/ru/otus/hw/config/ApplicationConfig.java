package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.props.application.ApplicationProps;

@Configuration
@EnableConfigurationProperties(ApplicationProps.class)
public class ApplicationConfig {

}
