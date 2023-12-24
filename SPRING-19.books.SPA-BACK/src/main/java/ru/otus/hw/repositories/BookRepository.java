package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @EntityGraph(value = "book-author-genres-entity-graph")
    @Query("SELECT b FROM Book b INNER JOIN b.genres g WHERE g.id = :genresId")
//    @Param("genresId")
    List<Book> findAllBooksByGenreId(long genresId);

}
