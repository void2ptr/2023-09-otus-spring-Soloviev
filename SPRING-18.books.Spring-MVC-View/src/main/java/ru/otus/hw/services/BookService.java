package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    List<AuthorDto> findAllAuthorsNotInBook(long bookId);

    List<GenreDto> findAllGenresNotInBook(long bookId);

    void insert(BookIdsDto bookIdsDto);

    void update(BookIdsDto bookIdsDto);

    void deleteById(long id);
}
