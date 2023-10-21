package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test CsvQuestionDao")
class CsvQuestionDaoTest {

    private final TestFileNameProvider mockTestFileNameProvider = mock(TestFileNameProvider.class);
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeEach
    void setUp() {
        when(mockTestFileNameProvider.testFileName()).thenReturn("questions_test.csv");
        csvQuestionDao = new CsvQuestionDao(mockTestFileNameProvider);
    }

    @DisplayName("method findAll() - Question && Answer are not empty")
    @Test
    void shouldReturnListOfQuestionsWithNotEmptyAnswers() {
        List<Question> questions = csvQuestionDao.findAll();
        // tested method
        assertThat(questions.stream()
                .noneMatch(question ->
                    question.text().isEmpty()
                            && question.answers().isEmpty()
                            && question.answers().stream()
                                .noneMatch(answer ->
                                           answer.text().isEmpty()
                                )
                )
        ).isTrue();
    }
}
