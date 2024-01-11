package ru.otus.hw.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;

public interface CommentService {
    Flux<CommentDto> findAllByBookId(Long bookId);

    Mono<CommentDto> insert(CommentDto commentDto);

    Mono<CommentDto> update(CommentDto commentDto);

    Mono<CommentDto> delete(long commentId);
}
