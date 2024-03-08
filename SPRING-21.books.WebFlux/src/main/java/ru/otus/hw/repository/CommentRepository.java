package ru.otus.hw.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.model.Comment;


public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {

    @Query("SELECT c.id, c.book_id, c.description  FROM comment c WHERE c.book_id = :id")
    Flux<Comment> findCommentsByBookId(@Param("id") Long bookId);

    @Query("SELECT c.id, c.book_id, c.description  FROM comment c WHERE c.id = :id")
    Mono<Comment> findCommentById(@Param("id") Long commentId);

}
