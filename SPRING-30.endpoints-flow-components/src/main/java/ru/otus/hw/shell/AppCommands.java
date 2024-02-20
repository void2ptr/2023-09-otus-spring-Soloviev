package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.InsectService;

import java.util.Map;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class AppCommands {

    private final ListableBeanFactory beanFactory;

    private final InsectService insectService;

    @ShellMethod(value = "info", key = "i")
    public void channelInfo() {
        Map<String, MessageChannel> channels = beanFactory.getBeansOfType(MessageChannel.class);
        log.info("CHANNELS:");
        int i = 0;
        for (Map.Entry<String, MessageChannel> entry : channels.entrySet()) {
            log.info("{}. {}/{} -> {}", ++i,
                    entry.getKey(),
                    entry.getValue().getClass().getSimpleName(),
                    entry.getValue());
        }
        log.info("HANDLERS:");
        i = 0;
        Map<String, MessageHandler> endpoints = beanFactory.getBeansOfType(MessageHandler.class);
        for (Map.Entry<String, MessageHandler> entry : endpoints.entrySet()) {
            log.info("{}. {}/{} -> {}", ++i,
                    entry.getKey(),
                    entry.getValue().getClass().getSimpleName(),
                    entry.getValue());
        }
    }

    @ShellMethod(value = "transformation", key = "t")
    public void startTransformation() {
        insectService.startMutation();
    }

}
