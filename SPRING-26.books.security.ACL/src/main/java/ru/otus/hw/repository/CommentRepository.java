package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Comment> findCommentsByBookId(long bookId);

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Comment', 'READ')")
    @EntityGraph("comment-book-entity-graph")
    Optional<Comment> findCommentById(Long id);

    @PostAuthorize("hasPermission(#id, 'ru.otus.hw.model.Comment', 'DELETE')")
    void deleteById(long id);

}
