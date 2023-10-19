package ru.otus.hw.config.props.messages;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.TestLocale;

@Service
@Getter
public class QuestionsPropsImp implements QuestionsProps, StudentProps {
    private final TestLocale locale;
    private final MessageSource messageSource;

    private final String firstName;
    private final String lastName;
    private final String questionPrompt;
    private final String answerPrompt;

    public QuestionsPropsImp(TestLocale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;

        this.firstName      = messageSource.getMessage("ask.firstName", new String[]{""}, locale.getLocale());
        this.lastName       = messageSource.getMessage("ask.lastName", new String[]{""}, locale.getLocale());
        this.questionPrompt = messageSource.getMessage("prompt.question", new String[]{""}, locale.getLocale());
        this.answerPrompt   = messageSource.getMessage("prompt.answer", new String[]{""}, locale.getLocale());
    }
}
