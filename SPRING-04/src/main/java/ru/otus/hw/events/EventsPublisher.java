package ru.otus.hw.events;

import ru.otus.hw.domain.TestResult;

public interface EventsPublisher {
    void publish(TestResult testResult);
}
