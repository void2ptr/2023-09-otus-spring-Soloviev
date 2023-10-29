package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.otus.hw.domain.TestResult;

@Component
@RequiredArgsConstructor
public class ExamEventPublisher implements EventsPublisher {

    @Autowired // Use setter based injection
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(TestResult testResult) {
        applicationEventPublisher.publishEvent(
                new ExamEvent(this, testResult)
        );
    }
}
