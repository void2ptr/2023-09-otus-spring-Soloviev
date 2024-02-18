package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Pupa {

    private String body;

    @Override
    public String toString() {
        return "Pupa{" +
                "body='" + body + '\'' +
                '}';
    }
}
