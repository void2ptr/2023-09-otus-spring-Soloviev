package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class TestServiceImplTest {
    private final static StreamsIOService stubStreamsIOService = mock(StreamsIOService.class);
    private final static CsvQuestionDao stubCsvQuestionDao = mock(CsvQuestionDao.class);
    private final static TestServiceImpl testServiceImpl = new TestServiceImpl(
            stubStreamsIOService,
            stubCsvQuestionDao
    );

    @Test
    void executeTestFor() {
        // init
        Student student = new Student("Wu", "Foo");
        TestResult expectedResult = new TestResult(student);

        List<Answer> answers_1 = new ArrayList<>();
        answers_1.add(new Answer("Answer A ?", false));
        answers_1.add(new Answer("Answer B ?", false));
        answers_1.add(new Answer("Answer C ?", true));
        Question question_1 = new Question("The question", answers_1);

        List<Answer> answers_2 = new ArrayList<>();
        answers_2.add(new Answer("Answer X ?", false));
        answers_2.add(new Answer("Answer Y ?", false));
        answers_2.add(new Answer("Answer Z ?", true));
        Question question_2 = new Question("The question", answers_2);

        List<Question> questions = new ArrayList<>();
        questions.add(question_1);
        questions.add(question_2);
        for (Question question : questions) {
            expectedResult.applyAnswer(question, true);
        }

        // mock
        when(stubCsvQuestionDao.findAll()).thenReturn(questions);
        when(stubStreamsIOService.readStringWithPrompt("Please input the correct answer number:")).thenReturn("3");

        // test
        TestResult actualResult = testServiceImpl.executeTestFor(student);

        assertEquals(expectedResult.getAnsweredQuestions(),actualResult.getAnsweredQuestions());
        assertEquals(expectedResult.getRightAnswersCount(),actualResult.getRightAnswersCount());
    }

    @Test
    void showQuestion() {
        // Init
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer 1 ?", false));
        answers.add(new Answer("Answer 2 ?", true));
        answers.add(new Answer("Answer 3 ?", false));
        Question question = new Question("The question", answers);
        String correctAnswer = testServiceImpl.showQuestion(question);
        // test
        assertNotEquals("1", correctAnswer);
        assertEquals("2", correctAnswer);
        assertNotEquals("3", correctAnswer);
    }

    @Test
    void askQuestion() {
        // Init
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer X ?", false));
        answers.add(new Answer("Answer Y ?", true));
        answers.add(new Answer("Answer Z ?", false));
        Question question = new Question("The question", answers);
        // mock
        when(stubStreamsIOService.readStringWithPrompt("Please input the correct answer number:")).thenReturn("2");
        // actual
        Boolean actualAnswer = testServiceImpl.askQuestion(question);
        // test
        assertEquals(true, actualAnswer);
    }
}
