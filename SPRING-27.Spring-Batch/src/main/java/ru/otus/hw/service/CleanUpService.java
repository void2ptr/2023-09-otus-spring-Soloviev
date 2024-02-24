package ru.otus.hw.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Slf4j
@Service
public class CleanUpService {
    @SuppressWarnings("unused")
    public void cleanUp() {
        log.info("Выполняю завершающие мероприятия...");
//        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}
