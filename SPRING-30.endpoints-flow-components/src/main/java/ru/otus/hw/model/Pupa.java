package ru.otus.hw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
@AllArgsConstructor
@Getter
@Setter
public class Pupa {

    private String nimfa;

    @Override
    public String toString() {
        return "Pupa{" +
                "nimfa='" + nimfa + '\'' +
                '}';
    }
}
