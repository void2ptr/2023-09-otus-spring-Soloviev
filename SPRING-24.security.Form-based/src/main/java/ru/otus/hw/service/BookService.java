package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

//    List<AuthorDto> findAllAuthorsNotInBook(long bookId);

    void insert(BookIdsDto bookIdsDto);

    void update(BookIdsDto bookIdsDto);

    void deleteById(long id);
}
