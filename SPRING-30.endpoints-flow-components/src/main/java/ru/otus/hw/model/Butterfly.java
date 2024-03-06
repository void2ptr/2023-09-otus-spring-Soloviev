package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
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
