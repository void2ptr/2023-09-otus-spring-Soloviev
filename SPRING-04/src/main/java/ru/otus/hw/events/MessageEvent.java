package ru.otus.hw.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageEvent extends ApplicationEvent {

    private final String message;

    public MessageEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
