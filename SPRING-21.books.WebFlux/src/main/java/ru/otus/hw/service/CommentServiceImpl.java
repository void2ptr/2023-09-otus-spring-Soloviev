package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Flux<CommentDto> findAllByBookId(Long bookId) {
        return commentRepository.findCommentsByBookId(bookId)
                .map(CommentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> insert(CommentDto commentDto) {
        return bookRepository.existsById(commentDto.getBook().getId())
                .filter(aBoolean -> aBoolean)
                .flatMap(isBookExist -> commentRepository.save(
                        new Comment(commentDto.getBook().getId(), commentDto.getDescription())))
                .doOnError(throwable -> Mono.error(
                        new EntityNotFoundException("ERROR: insert comment for book '%d'"
                                .formatted(commentDto.getBook().getId()))))
                .map(CommentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> update(CommentDto commentDto) {
        return bookRepository.existsById(commentDto.getBook().getId())
                .filter(aBoolean -> aBoolean)
                .flatMap(aBoolean -> commentRepository.existsById(commentDto.getId()))
                .filter(aBoolean -> aBoolean)
                .flatMap(comment -> commentRepository.save(
                        new Comment(commentDto.getId(), commentDto.getBook().getId(), commentDto.getDescription())))
                .map(CommentMapper::toDto);
    }

    @Override
    public Mono<Boolean> delete(long commentId) {
        return commentRepository.existsById(commentId)
                .doOnSuccess(isCommentExist -> {
                    if (isCommentExist) {
                        commentRepository.deleteById(commentId).subscribe();
                    }
                })
                .doOnError(throwable ->
                        Mono.error(new EntityNotFoundException("ERROR: Book '%d' not found".formatted(commentId))));
    }
}
