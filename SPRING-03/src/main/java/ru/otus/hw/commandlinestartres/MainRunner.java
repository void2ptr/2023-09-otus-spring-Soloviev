package ru.otus.hw.commandlinestartres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.otus.hw.service.TestRunnerService;

@Order(value=1)
@Component
public class MainRunner implements CommandLineRunner {
    @Autowired
    private ApplicationContext appContext;
    @Override
    public void run(String... args) {
//        var appArgs = Arrays.toString(args);

        TestRunnerService testRunnerService = appContext.getBean(TestRunnerService.class);
        testRunnerService.run();
    }
}
