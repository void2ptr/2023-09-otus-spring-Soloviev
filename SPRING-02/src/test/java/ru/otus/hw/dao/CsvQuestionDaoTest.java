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

    final private AppConfig stubAppConfig = mock(AppConfig.class);
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeEach
    void setUp() {
        when(stubAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        csvQuestionDao = new CsvQuestionDao(stubAppConfig);
    }

    @DisplayName("method findAll() - Question have the [text]")
    @Test
    void findAll_isQuestionNotEmpty() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions) {
            assertNotNull(question.text(), "is Question have Body ?" );
        }
//        assertThat(csvQuestionDao.findAll())
//                .as("check the Question")
//                .isNotEmpty()
//                .usingRecursiveComparison()
//                .withEqualsForFields()
//                ;

    }

    @DisplayName("method findAll() - Question have the [Answers]")
    @Test
    void findAll_isQuestionHaveAnswers() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions) {
            assertTrue(question.answers().size() > 0, "is Question have Answers ?" );
        }
    }

    @DisplayName("method findAll() - Question.Answer.text not empty")
    @Test
    void findAll_isAnswerTestNotEmpty() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions) {
            for (Answer answer: question.answers()) {
                // check field text
                assertNotNull(answer.text(), "is Answers.test not null ?" );
            }
        }
    }

    @DisplayName("method findAll() - Question.Answer.isCorrect not empty")
    @Test
    void findAll_isAnswerResultNotEmpty() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions) {
            for (Answer answer: question.answers()) {
                assertNotNull(answer.isCorrect(), "is the Answer.isCorrect not null ?");
            }
        }
    }
}
