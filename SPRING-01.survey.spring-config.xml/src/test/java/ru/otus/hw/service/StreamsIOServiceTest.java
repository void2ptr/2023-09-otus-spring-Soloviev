package ru.otus.hw.service;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StreamsIOService")
class StreamsIOServiceTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final StreamsIOService stream = new StreamsIOService(new PrintStream(outContent));

    @Test
    void printLine() {
        stream.printLine("Simple line");
        String expected = "Simple line";
        assertEquals(expected, outContent.toString().trim());
    }

    @Test
    void printFormattedLine() {
        stream.printFormattedLine("Formatted: [%s]", "line");
        String expected = "Formatted: [line]";
        assertEquals(expected, outContent.toString().trim());
    }
}
