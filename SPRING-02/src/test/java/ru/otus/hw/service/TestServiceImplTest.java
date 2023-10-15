package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private final static StreamsIOService stubStreamsIOService = mock(StreamsIOService.class);
    private final static CsvQuestionDao stubCsvQuestionDao = mock(CsvQuestionDao.class);
    private final static QuestionService stubQuestionService = mock(QuestionService.class);
    private final static TestServiceImpl testServiceImpl = new TestServiceImpl(
            stubStreamsIOService,
            stubCsvQuestionDao,
            stubQuestionService
    );

    @Test
    void executeTestFor() {
        // init
        Student student = new Student("Wu", "Foo");
        TestResult expectedResult = new TestResult(student);

        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer A ?", false));
        answers.add(new Answer("Answer B ?", false));
        answers.add(new Answer("Answer C ?", true));
        Question question = new Question("The question", answers);

        List<Question> questions = new ArrayList<>();
        questions.add(question);
        expectedResult.applyAnswer(question, true);

        // mock
        when(stubCsvQuestionDao.findAll()).thenReturn(questions);
        when(stubQuestionService.askQuestion("Please input the correct answer number:", question)).thenReturn(true);

        // tested method
        TestResult actualResult = testServiceImpl.executeTestFor(student);

        // test
        assertEquals(expectedResult.getAnsweredQuestions(),actualResult.getAnsweredQuestions());
        assertEquals(expectedResult.getRightAnswersCount(),actualResult.getRightAnswersCount());
    }
}
