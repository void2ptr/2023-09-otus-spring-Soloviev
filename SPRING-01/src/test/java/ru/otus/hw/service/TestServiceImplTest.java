package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Service TestServiceImpl")
class TestServiceImplTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final StreamsIOService stream = new StreamsIOService(new PrintStream(outContent));
    private AppConfig stubAppConfig = mock(AppConfig.class);
    private CsvQuestionDao stubCsvQuestionDao = mock(CsvQuestionDao.class);
    private TestServiceImpl service = new TestServiceImpl(stream, stubCsvQuestionDao);

    @DisplayName("Init the test")
    @BeforeEach
    void setUp() {
        // init
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer 1 ?", false));
        answers.add(new Answer("Answer 2 ?", true));
        answers.add(new Answer("Answer 3 ?", false));
        Question question = new Question("Question 1", answers);
        List<Question> questions = new ArrayList<>();
        questions.add(question);
        // mock
        when(stubAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        when(stubCsvQuestionDao.findAll()).thenReturn(questions);
    }

    @DisplayName("The questions contain the words")
    @Test
    void executeTest() {
        // tested method
        service.executeTest();
        // test
        String expected = outContent.toString();
        assertTrue( expected.contains("Question 1"), "is contain [Question 1] ?");
    }
}
