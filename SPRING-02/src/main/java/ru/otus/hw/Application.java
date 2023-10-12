package ru.otus.hw;

import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.service.TestRunnerService;
@ComponentScan( basePackages = "ru.otus.hw" )
@Configuration
public class Application {
    public static void main(String[] args) {

        //Создать контекст на основе Annotation/Java конфигурирования
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}