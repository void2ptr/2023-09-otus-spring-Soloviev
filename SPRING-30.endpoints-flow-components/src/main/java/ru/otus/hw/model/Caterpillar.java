package ru.otus.hw.model;


import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class Caterpillar {

    private String kind;


    public Caterpillar(String kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "Caterpillar{" +
                "kind='" + kind + '\'' +
                '}';
    }
}
