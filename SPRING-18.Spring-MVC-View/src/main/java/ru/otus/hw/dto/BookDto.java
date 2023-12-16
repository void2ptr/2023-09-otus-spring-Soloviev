package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BookDto {

    private long id;

    private String title;

    private Author author;

    private List<Genre> genres;

}
