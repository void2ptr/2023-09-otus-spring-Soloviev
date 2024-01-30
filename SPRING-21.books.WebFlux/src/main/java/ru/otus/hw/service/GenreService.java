package ru.otus.hw.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;

public interface GenreService {

    Flux<GenreDto> findAll();

    Mono<GenreDto> findById(Long id);

    Mono<GenreDto> insert(GenreDto genreDto);

    Mono<GenreDto> update(GenreDto genreDto);

    Mono<Boolean> delete(Long genreId);
}
