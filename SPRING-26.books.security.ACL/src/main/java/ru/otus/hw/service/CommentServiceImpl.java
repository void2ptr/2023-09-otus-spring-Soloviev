package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.repository.CommentRepository;
import ru.otus.hw.security.acl.PermissionService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long bookId) {
        return commentRepository.findCommentsByBookId(bookId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CommentDto> findCommentById(long commentId) {
        Optional<Comment> commentById = commentRepository.findCommentById(commentId);
        if (commentById.isEmpty()) {
            throw new EntityNotFoundException("Comment with id '%d' not found".formatted(commentId));
        }
        return Optional.of(CommentMapper.toDto(commentById.get()));
    }

    @Override
    public Optional<BookDto> findBookById(long bookId) {
        var bookOpt = bookRepository.findAllById(bookId);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
        }
        return Optional.of(BookMapper.toDto(bookOpt.get()));
    }


    @PreAuthorize("hasPermission(#comment, 'CREATE')")
    @Transactional
    @Override
    public void insert(Comment comment) {
        this.save(comment);
    }

    @PreAuthorize("hasPermission(#comment, 'WRITE')")
    @Transactional
    @Override
    public void update(Comment comment) {
        var commentOpt = commentRepository.findCommentById(comment.getId());
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment '%d' not found".formatted(comment.getId()));
        }
        this.save(comment);
    }

    @Transactional
    @Override
    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private void save(Comment comment) {
        Comment saved = commentRepository.save(comment);
        permissionService.addPermission(false, saved,
                List.of(BasePermission.READ, BasePermission.WRITE, BasePermission.DELETE));
    }
}
