package ru.otus.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;

@ConfigurationProperties(prefix = "application")
public class ApplicationProps {
    private final String message;
    private final Locale locale;

    @ConstructorBinding
    public ApplicationProps(String message, Locale locale) {
        this.message = message;
        this.locale = locale;
    }

    public String getMessage() {
        return message;
    }

    public Locale getLocale() {
        return locale;
    }
}
