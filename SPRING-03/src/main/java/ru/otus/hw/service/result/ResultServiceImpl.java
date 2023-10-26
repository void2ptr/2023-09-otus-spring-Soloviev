package ru.otus.hw.service.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.conf.ProviderAnswersToPass;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.helper.AnsiColors;
import ru.otus.hw.service.translate.ResourcesTranslator;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ProviderAnswersToPass providerAnswersToPass;

    private final IOService ioService;

    private final ResourcesTranslator translator;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printFormattedLine(
                translator.getProps("result.banner", AnsiColors.BLUE, AnsiColors.RED, AnsiColors.RESET),
                testResult.getStudent().getFullName(),
                testResult.getAnsweredQuestions().size(),
                testResult.getRightAnswersCount()
        );

        if (testResult.getRightAnswersCount() >= providerAnswersToPass.getRightAnswersCountToPass()) {
            ioService.printLine(translator.getProps("result.pass", AnsiColors.PURPLE, AnsiColors.RESET));
            return;
        }
        ioService.printLine(translator.getProps("result.error", AnsiColors.RED, AnsiColors.RESET));
    }
}
