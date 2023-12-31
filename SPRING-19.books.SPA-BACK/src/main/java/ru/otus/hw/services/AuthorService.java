package ru.otus.hw.services;

import ru.otus.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author findAuthorById(long id);

    Author insert(Author author);

    Author update(Author author);

    Author delete(long id);

}
