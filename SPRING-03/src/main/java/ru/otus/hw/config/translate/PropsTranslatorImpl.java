package ru.otus.hw.config.translate;

import org.springframework.context.MessageSource;
import ru.otus.hw.config.props.application.data.ProviderLocale;

public class PropsTranslatorImpl implements PropsTranslator {
    private final static String[] COLORS = AnsiColors.getColors();
    private final ProviderLocale locale;
    private final MessageSource messageSource;

    public PropsTranslatorImpl(ProviderLocale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;
    }

    @Override
    public String getProps(String code){
        return this.messageSource.getMessage(code, COLORS, this.locale.getLocale());
    }
}
