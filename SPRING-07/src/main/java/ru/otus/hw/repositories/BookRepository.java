package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join fetch b.author a left join fetch b.genres g where b.id = :id")
    Optional<Book> findById(long id);

    @NonNull
    @Query("select b from Book b left join fetch b.author a left join fetch b.genres g")
    List<Book> findAll();

    @Query
    void deleteById(@Param("id") long id);

}
