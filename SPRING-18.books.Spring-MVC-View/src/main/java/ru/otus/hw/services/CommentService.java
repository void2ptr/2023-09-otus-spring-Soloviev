package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long bookId);

    Optional<CommentDto> findCommentById(long commentId);

    Optional<BookDto> findBookById(long bookId);

    void insert(long bookId, String comment);

    void update(long bookId, long commentId, String comment);

    void delete(long commentId);
}
