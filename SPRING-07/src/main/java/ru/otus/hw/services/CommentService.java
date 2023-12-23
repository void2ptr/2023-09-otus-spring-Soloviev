package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long bookId);

    CommentDto insert(long bookId, String comment);

    CommentDto update(long bookId, long commentId, String comment);

    void delete(long commentId);
}
