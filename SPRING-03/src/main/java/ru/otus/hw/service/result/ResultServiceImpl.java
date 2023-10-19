package ru.otus.hw.service.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.TestConfig;
import ru.otus.hw.config.props.translate.TestResulBanner;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final IOService ioService;

    private final TestResulBanner testResulBanner;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printFormattedLine(testResulBanner.getResultBanner(),
                testResult.getStudent().getFullName(),
                testResult.getAnsweredQuestions().size(),
                testResult.getRightAnswersCount()
        );

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine(testResulBanner.getResultPass());
            return;
        }
        ioService.printLine(testResulBanner.getResultError());
    }
}
