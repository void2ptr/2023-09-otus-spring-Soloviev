package ru.otus.hw.service.exams;

import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
