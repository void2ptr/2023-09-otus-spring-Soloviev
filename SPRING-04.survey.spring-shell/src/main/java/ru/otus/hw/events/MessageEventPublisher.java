package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
    public class MessageEventPublisher implements EventsPublisher {

    @Autowired // Use setter based injection
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(String string) {
        applicationEventPublisher.publishEvent(
                new MessageEvent(this, string)
        );
    }
}
