package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.models.Book;

@AllArgsConstructor
@Getter
@Setter
public class CommentDto {

    private long id;

    private String description;

    private Book book;

}
