package ru.otus.hw.model;


import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class Caterpillar {

    private String body;


    public Caterpillar(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Caterpillar{" +
                "body='" + body + '\'' +
                '}';
    }
}
