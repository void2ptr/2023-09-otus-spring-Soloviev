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

    @BeforeAll
    public static void setUp(){
        AppConfig appConfig = new AppConfig("questions.csv");
        TestServiceImpl service = new TestServiceImpl(stream, appConfig);
        service.executeTest();
    }

    @DisplayName("My Questions contain the words")
    @Test
    void executeTest() {
        String content = outContent.toString();
        assertTrue( content.contains("JunHu"), "contain [JunHu]");
        assertTrue( content.contains("Three Kingdoms"), "contain [Three Kingdoms]");
        assertTrue( content.contains("Red Chamber"), "contain [Red Chamber]");
        assertTrue( content.contains("River Backwaters"), "contain [River Backwaters]");
        assertTrue( content.contains("Wu-Sung"), "contain [Wu-Sung]");
    }
}
