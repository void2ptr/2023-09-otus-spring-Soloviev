package ru.otus.hw.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;

public interface BookRepositoryCustom {

    Flux<BookDto> findAllBooks();

    Mono<BookDto> findByBookId(Long bookId);

    Mono<Boolean> existByAuthorId(Long authorId);
}
