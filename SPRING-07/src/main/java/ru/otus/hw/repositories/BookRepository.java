package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join fetch b.author a where b.id = :id")
    Optional<Book> findBookById(@Param("id") long id);

    @NonNull
    @Query("select b from Book b left join fetch b.author a")
    List<Book> findAllBooks();

}
