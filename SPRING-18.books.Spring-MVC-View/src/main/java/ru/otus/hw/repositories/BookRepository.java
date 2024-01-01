package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book-author-genres-entity-graph")
    Optional<Book> findAllById(long bookId);

    @NonNull
    @EntityGraph("book-author-entity-graph")
    List<Book> findAll();

    @EntityGraph("book-author-entity-graph")
    List<Book> findAllBooksByAuthorId(long authorId);

    @EntityGraph("book-author-genres-entity-graph")
    List<Book> findAllByGenresId(long genresId);

}
