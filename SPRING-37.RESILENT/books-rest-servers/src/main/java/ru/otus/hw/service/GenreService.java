package ru.otus.hw.service;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAll();

    Optional<GenreDto> findGenreById(long id);

    GenreDto insert(GenreDto genre);

    GenreDto update(GenreDto genre);

    GenreDto delete(long genreId);
}
