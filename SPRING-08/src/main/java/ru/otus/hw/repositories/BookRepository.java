package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, String> {

    @NonNull
    List<Book> findAll();

    @Query("{ 'title' : ?0 }")
    Optional<Book> findByTitleIs(String title);

    List<Book> findAllByTitleIs(String titles);

}
