package ru.otus.hw.config.props.translate;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.TestLocale;

@Service
@Getter
public class TranslatePropsImp implements QuestionsProps, StudentProps, BannerProps {
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_BLACK  = "\u001B[30m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    private final TestLocale locale;
    private final MessageSource messageSource;

    private final String firstName;
    private final String lastName;
    private final String questionPrompt;
    private final String answerPrompt;

    private final String resultBanner;
    private final String resultPass;
    private final String resultError;

    public TranslatePropsImp(TestLocale locale, MessageSource messageSource) {
        this.locale = locale;
        this.messageSource = messageSource;

        String[] ansi   = new String[]{ANSI_BLUE, ANSI_RED, ANSI_RESET };
        this.firstName      = messageSource.getMessage("ask.firstName", ansi, locale.getLocale());
        this.lastName       = messageSource.getMessage("ask.lastName", ansi, locale.getLocale());
        this.questionPrompt = messageSource.getMessage("prompt.question", ansi, locale.getLocale());
        this.answerPrompt   = messageSource.getMessage("prompt.answer", ansi, locale.getLocale());

        this.resultBanner   = messageSource.getMessage("result.banner", ansi, locale.getLocale());
        this.resultPass     = messageSource.getMessage("result.pass", ansi, locale.getLocale());
        this.resultError    = messageSource.getMessage("result.error", ansi, locale.getLocale());
    }
}
