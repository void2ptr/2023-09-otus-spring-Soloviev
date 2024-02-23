package ru.otus.hw.service;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long bookId);

    Optional<CommentDto> findCommentById(long commentId);

    Optional<BookDto> findBookById(long bookId);

    void insert(Comment comment);

    void update(Comment comment);

    void delete(long commentId);
}
