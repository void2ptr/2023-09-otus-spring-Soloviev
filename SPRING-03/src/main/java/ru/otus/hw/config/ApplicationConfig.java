package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.props.application.ApplicationProps;
import ru.otus.hw.config.props.application.TestLocale;
import ru.otus.hw.config.props.translate.TranslateProps;

@Configuration
@EnableConfigurationProperties(ApplicationProps.class)
public class ApplicationConfig {

    @Bean
    TranslateProps getTranslateProps(TestLocale locale, MessageSource messageSource){
        return new TranslateProps(locale, messageSource);
    }
}
