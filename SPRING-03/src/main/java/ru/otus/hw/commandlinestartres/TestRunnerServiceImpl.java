package ru.otus.hw.commandlinestartres;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.result.ResultService;
import ru.otus.hw.service.student.StudentService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService, CommandLineRunner {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run(String... args) {
        var appArgs = Arrays.toString(args);

        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
