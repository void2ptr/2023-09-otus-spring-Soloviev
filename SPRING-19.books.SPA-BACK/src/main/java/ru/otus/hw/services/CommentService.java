package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> findCommentByBookId(long bookId);

    CommentDto insert(CommentDto commentDto);

    CommentDto update(CommentDto commentDto);

    CommentDto delete(long commentId);
}
