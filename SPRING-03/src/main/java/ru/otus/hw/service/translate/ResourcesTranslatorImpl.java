package ru.otus.hw.service.translate;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.data.ProviderLocale;

@Service
public class ResourcesTranslatorImpl implements ResourcesTranslator {
    private final static String[] COLORS = AnsiColors.getColors();
    private final ProviderLocale locale;
    private final MessageSource messageSource;

    public ResourcesTranslatorImpl(ProviderLocale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;
    }

    @Override
    public String getProps(String code) {
        return this.messageSource.getMessage(code, COLORS, this.locale.getLocale());
    }
}
