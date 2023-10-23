package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.props.application.conf.PropsAppConfig;
import ru.otus.hw.config.props.application.data.PropsAppData;
import ru.otus.hw.config.props.application.data.ProviderLocale;
import ru.otus.hw.config.translate.PropsTranslator;
import ru.otus.hw.config.translate.PropsTranslatorImpl;

@Configuration
@EnableConfigurationProperties({
        PropsAppData.class,
        PropsAppConfig.class
})
public class ApplicationConfig {

    @Bean
    PropsTranslator getTranslateProps(ProviderLocale locale, MessageSource messageSource){
        return new PropsTranslatorImpl(locale, messageSource);
    }
}
