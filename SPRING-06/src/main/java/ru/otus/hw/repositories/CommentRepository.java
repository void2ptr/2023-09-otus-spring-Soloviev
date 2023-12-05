package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository {

    List<Comment> findCommentByBookId(long bookId);

    Optional<Comment> findCommentById(long commentId);

    Comment saveComment(Comment comment);

    void deleteComment(Comment comment);
}
