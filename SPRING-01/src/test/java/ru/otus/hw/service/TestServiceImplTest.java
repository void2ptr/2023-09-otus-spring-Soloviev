package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppConfig;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Service TestServiceImpl")
class TestServiceImplTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final StreamsIOService stream = new StreamsIOService(new PrintStream(outContent));

    @DisplayName("The questions [resource:file] contain the words")
    @Test
    void executeTest() {
        AppConfig appConfig = new AppConfig("questions_test.csv");
        TestServiceImpl service = new TestServiceImpl(stream, appConfig);
        service.executeTest();

        String content = outContent.toString();
        assertTrue( content.contains("Question 1"), "contain [Question 1]");
        assertTrue( content.contains("Question 2"), "contain [Question 2]");
        assertTrue( content.contains("Question 3"), "contain [Question 3]");
        assertTrue( content.contains("Question 4"), "contain [Question 4]");
        assertTrue( content.contains("Question 5"), "contain [Question 5]");
    }
}
