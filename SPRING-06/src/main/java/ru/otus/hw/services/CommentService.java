package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentByBookId(long bookId);

    Comment insert(long bookId, String comment);

    Comment update(long bookId, long commentId, String comment);

    void delete(long commentId);
}
