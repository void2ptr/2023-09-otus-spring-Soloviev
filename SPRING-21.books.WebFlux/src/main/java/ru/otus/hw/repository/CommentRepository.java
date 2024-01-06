//package ru.otus.hw.repository;
//
//import org.springframework.data.jpa.repository.EntityGraph;
//import org.springframework.data.repository.reactive.ReactiveCrudRepository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import ru.otus.hw.model.Comment;
//
//
//public interface CommentRepository extends ReactiveCrudRepository<Comment, Long> {
//
//    @EntityGraph("comment-book-entity-graph")
//    Flux<Comment> findCommentsByBookId(long bookId);
//
//    @EntityGraph("comment-book-entity-graph")
//    Mono<Comment> findCommentById(long commentId);
//
//}
