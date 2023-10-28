package ru.otus.hw.config.props.application.data;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;

@Getter
@ConfigurationProperties(prefix = "application.data")
public class PropsAppData implements ProviderFileName, ProviderLocale {
    private final Locale locale;
    private final int rightAnswersCountToPass;
    private final String testFileName;

    @ConstructorBinding
    public PropsAppData(Locale locale, int rightAnswersCountToPass, String testFileName) {
        this.locale = locale;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName
                + '.' + this.locale.toString()
                + ".csv";
    }
}
