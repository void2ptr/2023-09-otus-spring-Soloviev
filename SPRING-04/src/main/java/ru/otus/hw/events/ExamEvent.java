package ru.otus.hw.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.otus.hw.domain.TestResult;

@Getter
public class ExamEvent extends ApplicationEvent {

    private final TestResult testResult;

    public ExamEvent(Object source, TestResult testResult) {
        super(source);
        this.testResult = testResult;
    }
}
