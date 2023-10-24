package ru.otus.hw.service.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.conf.ProviderAnswersToPass;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.translate.PropsTranslator;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final ProviderAnswersToPass providerAnswersToPass;

    private final IOService ioService;

    private final PropsTranslator translator;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printFormattedLine(
                translator.getProps("result.banner"),
                testResult.getStudent().getFullName(),
                testResult.getAnsweredQuestions().size(),
                testResult.getRightAnswersCount()
        );

        if (testResult.getRightAnswersCount() >= providerAnswersToPass.getRightAnswersCountToPass()) {
            ioService.printLine(translator.getProps("result.pass"));
            return;
        }
        ioService.printLine(translator.getProps("result.error"));
    }
}
