package ru.otus.hw.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.helper.AnsiColors;
import ru.otus.hw.service.translate.MessagesTranslator;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final MessagesTranslator translator;
    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(
                translator.getProps("ask.firstName", AnsiColors.BLUE, AnsiColors.RESET )
        );
        var lastName = ioService.readStringWithPrompt(
                translator.getProps("ask.lastName", AnsiColors.BLUE, AnsiColors.RESET )
        );
        return new Student(firstName, lastName);
    }
}
