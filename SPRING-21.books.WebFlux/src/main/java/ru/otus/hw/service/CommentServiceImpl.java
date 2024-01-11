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

import java.util.Objects;

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
        return this.save(new Comment(commentDto.getBook().getId(), commentDto.getDescription()));
    }

    @Override
    public Mono<CommentDto> update(CommentDto commentDto) {
        commentRepository.findCommentById(commentDto.getId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: comment '%d' not found"
                .formatted(commentDto.getBook().getId()))))
                .block();
        return this.save(new Comment(commentDto.getId(), commentDto.getBook().getId(), commentDto.getDescription()));
    }

    @Override
    public Mono<CommentDto> delete(long commentId) {
        CommentDto commentBefore = commentRepository.findById(commentId)
                .map(CommentMapper::toDto)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: comment '%d' not found"
                        .formatted(commentId))))
                .block();

        commentRepository.deleteById(commentId).block();

        commentRepository.findById(commentId)
                .doOnSuccess(q -> Mono.error(new EntityNotFoundException("ERROR: comment '%d' do not deleted"
                        .formatted(commentId))))
                .block();

        return Mono.just(Objects.requireNonNull(commentBefore));
    }

    private Mono<CommentDto> save(Comment comment) {
        bookRepository.findById(comment.getBookId())
                .switchIfEmpty(Mono.error(new EntityNotFoundException("ERROR: book '%d' not found"
                        .formatted(comment.getBookId()))))
                .block();

        Comment commentSave = commentRepository.save(comment).block();

        return Mono.just(CommentMapper.toDto(Objects.requireNonNull(commentSave)));
    }
}
