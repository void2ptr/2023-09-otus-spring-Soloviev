package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentByBookId(String bookId);

    Comment insert(String bookId, String comment);

    Comment update(String bookId, String commentId, String comment);

    void delete(String commentId);
}
