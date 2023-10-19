package ru.otus.hw.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.props.translate.StudentProps;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final StudentProps studentConst;
    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(studentConst.getFirstName());
        var lastName = ioService.readStringWithPrompt(studentConst.getLastName());
        return new Student(firstName, lastName);
    }
}
