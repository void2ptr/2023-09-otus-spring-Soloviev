package ru.otus.hw.service.translate;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.data.ProviderLocale;

@Service
public class MessagesTranslatorImpl implements MessagesTranslator {
    private final ProviderLocale locale;
    private final MessageSource messageSource;

    public MessagesTranslatorImpl(ProviderLocale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;
    }

    @Override
    public String getProps(String code, Object ...args) {
        return this.messageSource.getMessage(code, args, this.locale.getLocale());
    }
}
