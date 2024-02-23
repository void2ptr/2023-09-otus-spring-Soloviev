package ru.otus.hw.service;

import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAll();

    Optional<GenreDto> findGenreById(long id);

    void insert(Genre genreDto);

    void update(Genre genreDto);

    void delete(long genreId);
}
