package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.properties")
public class AppConfig implements TestConfig, TestFileNameProvider {

    // внедрить свойство из application.properties
    @Value("${test.rightAnswersCountToPass:4}")
    private int rightAnswersCountToPass;

    // внедрить свойство из application.properties
    @Value("${test.fileName}")
    private String testFileName;

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }
}
