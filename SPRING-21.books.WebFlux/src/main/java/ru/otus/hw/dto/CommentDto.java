package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CommentDto {

    private Long id;

    private String description;

    private BookDto book;

}
