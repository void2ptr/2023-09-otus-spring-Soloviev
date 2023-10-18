package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.dao.CsvQuestionDao;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Service TestServiceImpl")
class TestServiceImplTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private StreamsIOService spyStream = mock(StreamsIOService.class, withSettings()
            .useConstructor(new PrintStream(outContent))
            .defaultAnswer(CALLS_REAL_METHODS)
    );
    private AppConfig mockAppConfig = mock(AppConfig.class);
    private CsvQuestionDao csvQuestionDao;
    private TestServiceImpl service;

    @DisplayName("Read [resource:questions_test.csv]")
    @BeforeEach
    void setUp() {
        // mock
        when(mockAppConfig.getTestFileName()).thenReturn("questions_test.csv");
        //
        csvQuestionDao = new CsvQuestionDao(mockAppConfig);
        service = new TestServiceImpl(spyStream, csvQuestionDao);
    }

    @DisplayName("he questions [resource:file] contain the words")
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
