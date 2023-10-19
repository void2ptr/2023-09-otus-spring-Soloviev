package ru.otus.hw.config.props.translate;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.TestLocale;
import ru.otus.hw.domain.AnsiColors;

@Service
@Getter
public class QuestionsPropsImp implements QuestionsProps, StudentProps, TestResulBanner {
    private final TestLocale locale;
    private final MessageSource messageSource;
    private final AnsiColors colors;

    private final String firstName;
    private final String lastName;
    private final String questionPrompt;
    private final String answerPrompt;

    private final String resultBanner;
    private final String resultPass;
    private final String resultError;

    public QuestionsPropsImp(TestLocale locale, MessageSource messageSource, AnsiColors colors) {
        this.locale = locale;
        this.messageSource = messageSource;
        this.colors = colors;

        String[] ansi   = new String[]{this.colors.getBlue(), this.colors.getRed(), this.colors.getReset() };
        String[] banner = new String[]{this.colors.getRed(), this.colors.getReset()};
        this.firstName      = messageSource.getMessage("ask.firstName", ansi, locale.getLocale());
        this.lastName       = messageSource.getMessage("ask.lastName", ansi, locale.getLocale());
        this.questionPrompt = messageSource.getMessage("prompt.question", ansi, locale.getLocale());
        this.answerPrompt   = messageSource.getMessage("prompt.answer", ansi, locale.getLocale());

        this.resultBanner   = messageSource.getMessage("result.banner", ansi, locale.getLocale());
        this.resultPass     = messageSource.getMessage("result.pass", banner, locale.getLocale());
        this.resultError    = messageSource.getMessage("result.error", banner, locale.getLocale());
    }
}
