package ru.otus.hw.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@SuppressWarnings("unused")
@AllArgsConstructor
@Getter
@Setter
public class Cocoon {

    private String silk;

    @Override
    public String toString() {
        return "Cocoon{" +
                "silk='" + silk + '\'' +
                '}';
    }
}
