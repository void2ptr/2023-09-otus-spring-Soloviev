package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.dao.CsvQuestionDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Service TestServiceImpl")
class TestServiceImplTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final StreamsIOService stream = new StreamsIOService(new PrintStream(outContent));
    private AppConfig stubAppConfig = mock(AppConfig.class);
    private CsvQuestionDao csvQuestionDao;
    private TestServiceImpl service;

    @DisplayName("Read [resource:questions_test.csv]")
    @BeforeEach
    void setUp(){
        when(stubAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        csvQuestionDao = new CsvQuestionDao(stubAppConfig);
        service = new TestServiceImpl(stream, csvQuestionDao);
    }

    @DisplayName("The questions [resource:file] contain the words")
    @Test
    void executeTest() {
        // tested method
        service.executeTest();
        // test
        String expected = outContent.toString();
        assertTrue( expected.contains("Question 1"), "contain [Question 1]");
        assertTrue( expected.contains("Question 2"), "contain [Question 2]");
        assertTrue( expected.contains("Question 3"), "contain [Question 3]");
        assertTrue( expected.contains("Question 4"), "contain [Question 4]");
        assertTrue( expected.contains("Question 5"), "contain [Question 5]");
    }
}
