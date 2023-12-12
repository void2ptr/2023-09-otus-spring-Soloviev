package ru.otus.hw.config;

public record TestProperties(int rightAnswersCountToPass, String testFileName
) implements TestConfig, TestFileNameProvider {}
