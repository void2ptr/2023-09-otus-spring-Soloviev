package ru.otus.hw.domain;

import java.util.List;
import java.util.stream.Collectors;

public record Question(String text, List<Answer> answers) {
    @Override
    public String toString() {
        return " " + text + "\n"
                + answers.stream()
                         .map(answer -> "  - " + answer.text() + "\n")
                         .collect(Collectors.joining());
    }
}
