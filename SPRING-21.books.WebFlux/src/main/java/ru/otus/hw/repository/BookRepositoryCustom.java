package ru.otus.hw.repository;

import reactor.core.publisher.Flux;
import ru.otus.hw.dto.BookDto;

public interface BookRepositoryCustom {

    Flux<BookDto> findAllBooks();

}
