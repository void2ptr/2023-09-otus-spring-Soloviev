package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    @Transactional(readOnly = true)
    Optional<Book> findById(String id);

    @Transactional(readOnly = true)
    List<Book> findAll();

    @Transactional
    Book insert(String title, List<String> authorNames, List<String> genresIds);

    @Transactional
    Book update(String title, List<String> authorNames, List<String> genresIds);

    @Transactional
    void deleteByTitle(String title);
}
