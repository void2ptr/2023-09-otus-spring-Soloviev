package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Optional<Book> findById(String id);

    List<Book> findAll();

    Book insert(String title, List<String> authorNames, List<String> genresIds);

    Book update(String title, List<String> authorNames, List<String> genresIds);

    void deleteByTitle(String title);
}
