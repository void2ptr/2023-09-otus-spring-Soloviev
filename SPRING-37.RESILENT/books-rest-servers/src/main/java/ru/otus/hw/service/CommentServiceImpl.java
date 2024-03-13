package ru.otus.hw.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @CircuitBreaker(name = "ControllerCommentFindCommentsByBookById", fallbackMethod = "recoverFindCommentsByBookById")
    @Override
    public List<CommentDto> findCommentByBookId(long bookId) {
        return commentRepository.findCommentsByBookId(bookId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Transactional
    @CircuitBreaker(name = "ControllerCommentInsert", fallbackMethod = "recoverInsert")
    @Override
    public CommentDto insert(CommentDto commentDto) {
        return CommentMapper.toDto(this.save(commentDto.getBook().getId(), 0, commentDto.getDescription()));
    }

    @Transactional
    @CircuitBreaker(name = "ControllerCommentUpdate", fallbackMethod = "recoverUpdate")
    @Override
    public CommentDto update(CommentDto commentDto) {
        var commentOpt = commentRepository.findCommentById(commentDto.getId());
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentDto.getBook().getId()));
        }
        Comment comment = this.save(commentDto.getBook().getId(), commentDto.getId(), commentDto.getDescription());
        return CommentMapper.toDto(comment);
    }

    @Transactional
    @CircuitBreaker(name = "ControllerCommentDelete", fallbackMethod = "recoverDelete")
    @Override
    public CommentDto delete(long commentId) {
        Comment commentBefore = commentRepository.findCommentById(commentId).orElseThrow(() ->
                new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentId)));
        commentRepository.deleteById(commentId);
        Optional<Comment> commentAfter = commentRepository.findCommentById(commentId);
        if (commentAfter.isPresent()) {
            throw new EntityNotFoundException("ERROR: comment '%d' do not deleted".formatted(commentId));
        }
        return CommentMapper.toDto(commentBefore);
    }

    private Comment save(long bookId, long commentId, String description) {
        var book = bookRepository.findAllById(bookId).orElseThrow(() ->
                new EntityNotFoundException("ERROR: book '%d' not found".formatted(bookId)));
        return commentRepository.save(new Comment(commentId, description, book));
    }

    private List<CommentDto> recoverFindCommentsByBookById(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return findCommentByBookId(id);
    }

    private CommentDto recoverInsert(CommentDto commentDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return insert(commentDto);
    }

    private CommentDto recoverUpdate(CommentDto commentDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return update(commentDto);
    }

    private CommentDto recoverDelete(long commentId, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return delete(commentId);
    }
}
