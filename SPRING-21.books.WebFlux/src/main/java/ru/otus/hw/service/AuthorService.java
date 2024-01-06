package ru.otus.hw.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

public interface AuthorService {
    Flux<AuthorDto> findAll();

    Mono<AuthorDto> findAuthorById(Long id);

    Mono<AuthorDto> insert(Author author);

    Mono<AuthorDto> update(Author author);

    Mono<AuthorDto> delete(Long id);

}
