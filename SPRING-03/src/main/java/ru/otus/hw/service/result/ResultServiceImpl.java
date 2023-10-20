package ru.otus.hw.service.result;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.props.application.TestAnswersToPass;
import ru.otus.hw.config.props.translate.BannerProps;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestAnswersToPass testAnswersToPass;

    private final IOService ioService;

    private final BannerProps banner;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printFormattedLine(
                banner.getResultBanner(),
                testResult.getStudent().getFullName(),
                testResult.getAnsweredQuestions().size(),
                testResult.getRightAnswersCount()
        );

        if (testResult.getRightAnswersCount() >= testAnswersToPass.getRightAnswersCountToPass()) {
            ioService.printLine(banner.getResultPass());
            return;
        }
        ioService.printLine(banner.getResultError());
    }
}
