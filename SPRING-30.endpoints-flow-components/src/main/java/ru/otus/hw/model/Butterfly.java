package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Butterfly {

    private String kind;

    @Override
    public String toString() {
        return "Butterfly{" +
                "kind='" + kind + '\'' +
                '}';
    }
}
