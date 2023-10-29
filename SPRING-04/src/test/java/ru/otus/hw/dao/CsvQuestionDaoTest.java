package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.props.application.data.ProviderFileName;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@DisplayName("Test CsvQuestionDao")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockBean
    private ProviderFileName mockProviderFileName;
    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @DisplayName("Test init")
    @BeforeEach
    void setUp() {
        mockProviderFileName = mock(ProviderFileName.class);
        given(mockProviderFileName.getTestFileName()).willReturn("data/questions.csv");
        csvQuestionDao = new CsvQuestionDao(mockProviderFileName);
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
                                .allMatch(answer -> !answer.text().isEmpty())
                );
    }
}
