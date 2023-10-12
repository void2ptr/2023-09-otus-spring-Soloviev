package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
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
            var isAnswerValid = false; // Задать вопрос, получить ответ

            Integer i = 0;
            String correctAnswer = "0";

            ioService.printFormattedLine(" %s ", question.text() );
            for (Answer answer : question.answers() ) {
                ioService.printFormattedLine("   %s - %s ", ++i, answer.text() );
                if ( answer.isCorrect() )  {
                    correctAnswer = i.toString();
                }
            }

            String s = ioService.readStringWithPrompt(" enter the response number >> ");
            if ( s.contentEquals( correctAnswer ) ) {
                isAnswerValid = true;
            } else {
                isAnswerValid = false;
            }

            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
