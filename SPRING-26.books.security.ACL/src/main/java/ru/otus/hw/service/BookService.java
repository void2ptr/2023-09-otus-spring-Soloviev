package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookDto> findAll();

    Optional<BookDto> findById(long id);

    void insert(Book book);

    void update(Book book);

    void deleteById(long id);

}
