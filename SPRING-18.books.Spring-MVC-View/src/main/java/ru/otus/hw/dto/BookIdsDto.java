package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BookIdsDto {

    private long id;

    private String title;

    private long authorId;

    private List<Long> genresId;

}
