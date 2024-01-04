package ru.otus.hw.repositorie;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByBookId(long bookId);

    @EntityGraph("comment-book-entity-graph")
    Optional<Comment> findCommentById(long commentId);

}
