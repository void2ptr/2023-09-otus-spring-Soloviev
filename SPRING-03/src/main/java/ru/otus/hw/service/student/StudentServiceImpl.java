package ru.otus.hw.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.translate.PropsTranslator;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final PropsTranslator translate;
    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(translate.getProps("ask.firstName"));
        var lastName = ioService.readStringWithPrompt(translate.getProps("ask.lastName"));
        return new Student(firstName, lastName);
    }
}
