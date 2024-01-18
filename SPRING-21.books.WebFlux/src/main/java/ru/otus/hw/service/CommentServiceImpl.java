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
        return bookRepository.findById(commentDto.getBook().getId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: book '%d' not found"
                                .formatted(commentDto.getBook().getId()))))
                .flatMap(book -> commentRepository.save(
                        new Comment(commentDto.getBook().getId(), commentDto.getDescription())))
                .map(CommentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> update(CommentDto commentDto) {
        return commentRepository.findCommentById(commentDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: comment '%d' not found"
                        .formatted(commentDto.getBook().getId()))))
                .doOnSuccess(comment -> bookRepository.findById(comment.getBookId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: book '%d' not found"
                                .formatted(comment.getBookId())))))
                .flatMap(comment -> commentRepository.save(
                        new Comment(commentDto.getId(), commentDto.getBook().getId(), commentDto.getDescription())))
                .map(CommentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> delete(long commentId) {
        return commentRepository.findById(commentId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: comment '%d' not found"
                        .formatted(commentId))))
                .doOnSuccess(comment -> commentRepository.deleteById(commentId))
                .doOnSuccess(comment -> commentRepository.findById(commentId)
                        .doOnSuccess(q -> Mono.error(new EntityNotFoundException("ERROR: comment '%d' do not deleted"
                                .formatted(commentId)))))
                .map(CommentMapper::toDto);
    }
}
