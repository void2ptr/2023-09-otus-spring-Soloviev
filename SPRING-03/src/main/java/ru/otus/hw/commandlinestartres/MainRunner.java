package ru.otus.hw.commandlinestartres;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.hw.Application;
import ru.otus.hw.service.TestRunnerService;

@Order(value=1)
@Component
@RequiredArgsConstructor
public class MainRunner implements CommandLineRunner {

    private final TestRunnerService testRunnerService;

    @Override
    public void run(String... args) {
//        var appArgs = Arrays.toString(args);

        testRunnerService.run();
    }
}
