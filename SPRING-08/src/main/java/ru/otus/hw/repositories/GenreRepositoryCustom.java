package ru.otus.hw.repositories;

import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepositoryCustom {

    List<Genre> findGenresByNames(List<String> genreNames);

}
