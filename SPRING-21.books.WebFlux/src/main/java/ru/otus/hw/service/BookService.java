package ru.otus.hw.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;

public interface BookService {

    Flux<BookDto> findAll();

    Mono<BookDto> findById(BookDto bookDto);

    Mono<BookDto> insert(BookDto bookDto);

    Mono<BookDto> update(BookDto bookDto);

    Mono<BookDto> delete(Long id);
}
