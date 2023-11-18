package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentByBookId(long id);
}
