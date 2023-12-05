package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findBookById(long id);

    List<Book> findAllBooks();

    Book saveBook(Book book);

    void deleteBookById(long id);

}
