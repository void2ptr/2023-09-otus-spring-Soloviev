package ru.otus.hw.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class Cocoon {

    private String cocoon;

    @Override
    public String toString() {
        return "Cocoon{" +
                "cocoon='" + cocoon + '\'' +
                '}';
    }
}
