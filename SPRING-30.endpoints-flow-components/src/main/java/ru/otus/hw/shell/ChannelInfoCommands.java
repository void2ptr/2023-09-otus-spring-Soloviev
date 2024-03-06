package ru.otus.hw.shell;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Map;


@SuppressWarnings("unused")
@ShellComponent
@Slf4j
public class ChannelInfoCommands {

    private final Map<String, MessageChannel> channels;

    private final Map<String, MessageHandler> endpoints;

    public ChannelInfoCommands(@Lazy Map<String, MessageChannel> channels,
                               @Lazy Map<String, MessageHandler> endpoints) {
        this.channels = channels;
        this.endpoints = endpoints;
    }

    @ShellMethod(value = "info", key = "i")
    public void channelInfo() {
        log.info("CHANNELS:");
        int i = 0;
        for (Map.Entry<String, MessageChannel> entry : channels.entrySet()) {
            log.info("{}. {}/{} -> {}", ++i,
                    entry.getKey(),
                    entry.getValue().getClass().getSimpleName(),
                    entry.getValue());
        }
    }

    @ShellMethod(value = "handler", key = "h")
    public void handlerInfo() {
        log.info("HANDLERS:");
        int i = 0;
        for (Map.Entry<String, MessageHandler> entry : endpoints.entrySet()) {
            log.info("{}. {}/{} -> {}", ++i,
                    entry.getKey(),
                    entry.getValue().getClass().getSimpleName(),
                    entry.getValue());
        }
    }
}
