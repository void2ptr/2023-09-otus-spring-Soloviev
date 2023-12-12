package ru.otus.hw.config.props.application.conf;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "application.config")
public class PropsAppConfig implements ProviderAnswersToPass {
    private final int rightAnswersCountToPass;

    @ConstructorBinding
    public PropsAppConfig(int rightAnswersCountToPass) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }
}
