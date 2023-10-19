package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.io.IOService;
import ru.otus.hw.config.props.translate.QuestionsProps;
import ru.otus.hw.service.questions.QuestionsService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionsService questionService;

    private final QuestionsProps questionsConst;

    @Override
    public TestResult executeTestFor(@Value("${student}") Student student) {
        ioService.printLine("");
        ioService.printFormattedLine(questionsConst.getQuestionPrompt());
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = questionService.chooseAnswer(question);

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
