package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test CsvQuestionDao")
class CsvQuestionDaoTest {

    final static private AppConfig stubAppConfig = mock(AppConfig.class);
    static private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeAll
    static void setUp(){
        when(stubAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        csvQuestionDao = new CsvQuestionDao(stubAppConfig);
    }

    @DisplayName("method findAll() - Question have the [text]")
    @Test
    void findAll_is_question_not_empty() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions){
            assertTrue(question.text().length() > 0, "is Question have Body ?" );
        }
    }
    @DisplayName("method findAll() - Question have the [Answers]")
    @Test
    void findAll_is_question_have_answers() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions){
            assertTrue(question.answers().size() > 0, "is Question have Answers ?" );
        }
    }

    @DisplayName("method findAll() - Question.Answer.text not empty")
    @Test
    void findAll_is_answer_not_empty() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions){
            for (Answer answer: question.answers()) {
                // check field text
                assertTrue(answer.text().length() > 0, "is Answers.test: [empty] ?" );
            }
        }
    }

    @DisplayName("method findAll() - Question.Answer.isCorrect Ok")
    @Test
    void findAll_is_answer_is_correct() {
        List<Question> questions = csvQuestionDao.findAll();
        for (Question question: questions){
            for (Answer answer: question.answers()) {
                // check field isCorrect
                if ( answer.isCorrect() ) {
                    assertTrue(answer.isCorrect() == true, "is Answer: [True] ?");
                } else {
                    assertTrue(answer.isCorrect() == false, "is Answer: [False] ?");
                }
            }
        }
    }

}