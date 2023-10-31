package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.props.application.data.PropsAppData;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@DisplayName("Test CsvQuestionDao")
@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockBean
    private PropsAppData mockPropsAppData;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeEach
    void setUp() {
        given(mockPropsAppData.getTestFileName()).willReturn("data/questions.csv");
    }

    @Test
    void findAll() {
        // tested method
        List<Question> questions = csvQuestionDao.findAll();
        // test
        assertThat(questions)
                .noneMatch(question -> question.text().isEmpty())
                .noneMatch(question -> question.answers().isEmpty())
                .allMatch(question ->
                        question.answers().stream()
                                .noneMatch(answer -> answer.text().isEmpty())
                );
    }
}
