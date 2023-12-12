package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.service.questions.QuestionsService;
import ru.otus.hw.helper.AnsiColors;
import ru.otus.hw.service.translate.MessagesTranslator;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionsService questionService;

    private final MessagesTranslator translator;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine(translator.getProps("prompt.question", AnsiColors.CYAN, AnsiColors.RESET));
        var questions = questionDao.findAll();
        questionService.randomizeQuestions(questions);
        var testResult = new TestResult(student);

        for (var question : questions) {
            var isAnswerValid = questionService.isAnswerValid(question);

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
