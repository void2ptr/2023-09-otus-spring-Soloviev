package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookDto bookDto);

    BookDto update(BookDto bookDto);

    BookDto delete(long id);
}
