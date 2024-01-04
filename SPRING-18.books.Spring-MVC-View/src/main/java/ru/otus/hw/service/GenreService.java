package ru.otus.hw.service;

import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<GenreDto> findAll();

    Optional<GenreDto> findGenreById(long id);

    List<GenreDto> findGenresNotInBook(long bookId);

    void insert(GenreDto genreDto);

    void update(GenreDto genreDto);

    void delete(long genreId);
}
