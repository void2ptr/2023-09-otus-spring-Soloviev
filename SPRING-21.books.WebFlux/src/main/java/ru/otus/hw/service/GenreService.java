package ru.otus.hw.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {

    Flux<GenreDto> findAll();

    Mono<GenreDto> findGenreById(Long id);

    Mono<GenreDto> insert(Genre genre);

    Mono<GenreDto> update(Genre genre);

    Mono<GenreDto> delete(Long genreId);
}
