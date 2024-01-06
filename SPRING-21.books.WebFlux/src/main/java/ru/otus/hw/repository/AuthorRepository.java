package ru.otus.hw.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Author;

public interface AuthorRepository extends ReactiveCrudRepository<Author, Long> {

    Mono<Author> findAuthorById(Long id);


    @Query("delete from Author a where a.id = $1")
    Mono<Void> deleteById(Long id);
}
