package ru.otus.hw.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.translate.ResourcesTranslator;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final ResourcesTranslator translator;
    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(translator.getProps("ask.firstName"));
        var lastName = ioService.readStringWithPrompt(translator.getProps("ask.lastName"));
        return new Student(firstName, lastName);
    }
}
