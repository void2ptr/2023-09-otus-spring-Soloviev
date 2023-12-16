package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph("book-author-genres-entity-graph")
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findBookById(@Param("id") long id);

    @NonNull
    @EntityGraph("book-author-entity-graph")
    @Query("select b from Book b")
    List<Book> findAllBooks();

    @EntityGraph("book-author-entity-graph")
    @Query("select b from Book b where b.id = :id")
    List<Book> findAllBooksByAuthorId(@Param("authorId") long id);
}
