package ru.otus.hw.service;

import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    AuthorDto findAuthorById(long id);

    Optional<Author> insert(Author author);

    Optional<Author> update(Author author);

    boolean delete(long authorId);

}
