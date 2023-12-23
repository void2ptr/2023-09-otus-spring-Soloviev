package ru.otus.hw.services;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    AuthorDto findAuthorsById(long authorId);

    List<AuthorDto> findAllAuthorsNotInBook(long bookId);

    GenreDto findGenresById(long genreId);

    List<GenreDto> findAllGenresNotInBook(long bookId);

    void insert(String title, long authorId, List<Long> genresIds);

    void update(long id, String title, long authorId, List<Long> genresIds);

    void deleteById(long id);
}
