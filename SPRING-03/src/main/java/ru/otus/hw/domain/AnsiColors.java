package ru.otus.hw.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AnsiColors {
    private final String reset = "\u001B[0m";
    private final String black = "\u001B[30m";
    private final String red = "\u001B[31m";
    private final String green = "\u001B[32m";
    private final String yellow = "\u001B[33m";
    private final String blue = "\u001B[34m";
    private final String purple = "\u001B[35m";
    private final String cyan = "\u001B[36m";
    private final String white = "\u001B[37m";
}
