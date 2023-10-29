package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.events.EventsPublisher;
import ru.otus.hw.helper.AnsiColors;
import ru.otus.hw.service.exams.TestService;
import ru.otus.hw.service.translate.MessagesTranslator;

@ShellComponent
@RequiredArgsConstructor
public class ExaminationCommands {
    private final EventsPublisher eventsPublisher;
    private final MessagesTranslator translator;
    private final TestService testService;
    private Student student;
    private TestResult testResult;

    @ShellMethod(value = "Login user [first name] [last name]", key = {"l", "login"})
    public String getLogin(
            @ShellOption(defaultValue = "John") String firstName,
            @ShellOption(defaultValue = "Doe") String lastName
    ) {
        this.student = new Student(firstName, lastName);
        this.testResult = null;

        return String.format(
                translator.getProps("shell.login.ok", AnsiColors.BLUE, AnsiColors.RESET),
                firstName, lastName
        );
    }

    @ShellMethod(value = "Run tests", key = {"t", "test"})
    @ShellMethodAvailability(value = "isUserLogin")
    public String executeTest() {

        testResult = testService.executeTestFor(student);
        eventsPublisher.publish(testResult);

        return String.format(
                translator.getProps("shell.exams.end", AnsiColors.PURPLE, AnsiColors.RESET),
                student.getFullName()
        );
    }

    private Availability isUserLogin() {
        return (this.student == null)
                ? Availability.unavailable(translator.getProps("shell.login.no"))
                : Availability.available();
    }

    @ShellMethod(value = "show examination result", key = {"s", "show"})
    @ShellMethodAvailability(value = "isResultReady")
    public String showResult() {
        eventsPublisher.publish(testResult);
        return String.format(
                translator.getProps("shell.exams.ready", AnsiColors.PURPLE, AnsiColors.RESET)
        );
    }

    private Availability isResultReady() {
        return (testResult == null)
                ? Availability.unavailable(translator.getProps("shell.exams.notReady"))
                : Availability.available();
    }
}
