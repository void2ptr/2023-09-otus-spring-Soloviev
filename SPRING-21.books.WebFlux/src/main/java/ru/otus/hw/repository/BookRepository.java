package ru.otus.hw.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Book;

public interface BookRepository extends ReactiveCrudRepository<Book, Long>, BookRepositoryCustom {

    @Query("SELECT b.id FROM book b WHERE b.author_id = :author_id")
    Mono<Book> findByAuthorId(@Param("author_id") Long authorId);
}
