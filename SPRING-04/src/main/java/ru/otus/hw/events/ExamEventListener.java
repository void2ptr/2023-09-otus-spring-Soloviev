package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.result.ResultService;

@Component
@RequiredArgsConstructor
public class ExamEventListener implements ApplicationListener<ExamEvent> {

    private final ResultService resultService;

    @Override
    public void onApplicationEvent(ExamEvent event) {
        resultService.showResult(event.getTestResult());
    }
}
