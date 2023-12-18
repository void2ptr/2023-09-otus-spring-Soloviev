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
    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Optional<Book> findBookById(@Param("bookId") long bookId);

    @NonNull
    @EntityGraph("book-author-entity-graph")
    @Query("SELECT b FROM Book b")
    List<Book> findAllBooks();

    @EntityGraph("book-author-entity-graph")
    @Query("SELECT b FROM Book b INNER JOIN b.author a WHERE a.id = :authorId")
    List<Book> findAllBooksByAuthorId(@Param("authorId") long authorId);

    @EntityGraph("book-author-genres-entity-graph")
    @Query("SELECT b FROM Book b INNER JOIN b.genres g WHERE g.id = :genresId")
    List<Book> findAllBooksByGenreId(@Param("genresId") long genresId);

}
