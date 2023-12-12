package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.props.application.conf.PropsAppConfig;
import ru.otus.hw.config.props.application.data.PropsAppData;

@Configuration
@EnableConfigurationProperties({
        PropsAppData.class,
        PropsAppConfig.class
})
public class ApplicationConfig {

}
