package ru.otus.hw.service.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.service.exams.TestService;
import ru.otus.hw.service.result.ResultService;
import ru.otus.hw.service.runner.TestRunnerService;
import ru.otus.hw.service.student.StudentService;

@Service
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = studentService.determineCurrentStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
