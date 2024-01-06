package ru.otus.hw.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Genre;

import java.util.List;


public interface GenreRepository extends ReactiveCrudRepository<Genre, Long> {

    Mono<Genre> findGenreById(Long id);

    Flux<Genre> findGenresByIdIn(List<Long> id);

}
