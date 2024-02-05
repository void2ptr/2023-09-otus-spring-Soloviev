package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    @NonNull
    @EntityGraph("book-author-entity-graph")
    List<Book> findAll();

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'READ')")
    @EntityGraph("book-author-genres-entity-graph")
    Optional<Book> findAllById(Long id);

    @PostFilter("hasPermission(filterObject, 'READ')")
    @EntityGraph("book-author-genres-entity-graph")
    List<Book> findAllByGenresId(Long id);

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Book', 'DELETE')")
    void deleteById(long id);

}
