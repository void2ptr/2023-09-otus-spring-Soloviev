package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test CsvQuestionDao")
class CsvQuestionDaoTest {

    private final AppConfig stubAppConfig = mock(AppConfig.class);
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeEach
    void setUp() {
        when(stubAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        csvQuestionDao = new CsvQuestionDao(stubAppConfig);
    }

    @DisplayName("method findAll() - Question.Answer.text not empty")
    @Test
    void findAll_isQuestionTextNotEmpty() {
        // tested method
        List<Question> questions = csvQuestionDao.findAll();
        // test
        assertThat(questions.stream()
                .noneMatch(question -> question.text().isEmpty())
        ).isTrue();
    }

    @DisplayName("method findAll() - Question have the [Answers]")
    @Test
    void findAll_isQuestionAnswerNotEmpty() {
        List<Question> questions = csvQuestionDao.findAll();
        assertThat(questions.stream()
                .noneMatch(question -> question.answers().isEmpty() )
        ).isTrue();
    }

    @DisplayName("method findAll() - Question.Answer.isCorrect [true] exist")
    @Test
    void findAll_isAnswerHave() {
        int correctAnswers = 0;
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions) {
            for (Answer answer: question.answers()) {
                if (answer.isCorrect()) {
                    ++correctAnswers;
                }
            }
        }
        // test
        assertTrue(correctAnswers > 0, "is the Answer.isCorrect [true] exist ?");
    }
}
