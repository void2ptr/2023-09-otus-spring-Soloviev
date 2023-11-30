package ru.otus.hw.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, String> {

    Optional<Book> findById(String id);

    Optional<Book> findByTitleIs(String title);

    @NonNull
    List<Book> findAll();

    void deleteById(String id);

}
