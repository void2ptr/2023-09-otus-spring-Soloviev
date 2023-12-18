package ru.otus.hw.services;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAll();

    Optional<GenreDto> findGenreById(long id);

    void insert(GenreDto genreDto);

    void update(GenreDto genreDto);

    void delete(long genreId);
}
