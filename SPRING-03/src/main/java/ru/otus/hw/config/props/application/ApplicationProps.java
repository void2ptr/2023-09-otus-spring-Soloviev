package ru.otus.hw.config.props.application;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;

@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProps implements TestAnswersToPass, TestFileName, TestLocale {
    private final String message;
    private final Locale locale;
    private final int rightAnswersCountToPass;
    private final String testFileName;

    @ConstructorBinding
    public ApplicationProps(String message, Locale locale, int rightAnswersCountToPass, String testFileName) {
        this.message = message;
        this.locale = locale;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName
                + '.' + this.locale.toString()
                + ".csv";
    }
}
