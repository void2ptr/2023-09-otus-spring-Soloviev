package ru.otus.hw.services;

import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    List<Genre> findAll();

    Optional<Genre> findGenreById(long id);

    Genre insert(Genre genre);

    Genre update(Genre genre);

    Genre delete(long genreId);
}
