package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph("comment-book-entity-graph")
    List<Comment> findCommentsByBookId(long bookId);

    @EntityGraph("comment-book-entity-graph")
    Optional<Comment> findCommentById(long commentId);

}
