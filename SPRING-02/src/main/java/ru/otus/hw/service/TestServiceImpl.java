package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(@Value("${student}") Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var isAnswerValid = askQuestion(question);

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    public String showQuestion(Question question) {
        String correctAnswer = "-1";
        Integer actualAnswer = 0;

        ioService.printFormattedLine("%s ", question.text());
        for (Answer answer : question.answers()) {
            ioService.printFormattedLine("   %s. %s ", ++actualAnswer, answer.text());
            if (answer.isCorrect())  {
                correctAnswer = actualAnswer.toString();
            }
        }

        return correctAnswer;
    }

    public Boolean askQuestion(Question question) {
        Boolean isAnswerValid;
        String correctAnswer = showQuestion(question);

        String userAnswer = ioService.readStringWithPrompt("Please input the correct answer number:");
        if (userAnswer.contentEquals(correctAnswer)) {
            isAnswerValid = true;
        } else {
            isAnswerValid = false;
        }
        return isAnswerValid;
    }
}
