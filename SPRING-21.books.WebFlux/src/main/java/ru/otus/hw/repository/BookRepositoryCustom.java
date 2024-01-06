package ru.otus.hw.repository;

import reactor.core.publisher.Flux;
import ru.otus.hw.model.BookModel;

public interface BookRepositoryCustom {

    Flux<BookModel> findAll();
}
