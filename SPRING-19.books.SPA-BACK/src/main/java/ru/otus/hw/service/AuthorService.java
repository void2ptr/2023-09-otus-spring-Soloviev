package ru.otus.hw.service;

import ru.otus.hw.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();

    Author findAuthorById(long id);

    Author insert(Author author);

    Author update(Author author);

    Author delete(long id);

}
