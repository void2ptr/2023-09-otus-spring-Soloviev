package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repositorie.BookRepository;
import ru.otus.hw.repositorie.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

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
        var book = bookRepository.findAllById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d not found".formatted(bookId));
        }
        return Optional.of(BookMapper.toDto(book.get()));
    }

    @Transactional
    @Override
    public void insert(long bookId, String description) {
        this.save(bookId, 0, description);
    }

    @Transactional
    @Override
    public void update(long bookId, long commentId, String description) {
        var comment = commentRepository.findCommentById(commentId);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentId));
        }
        this.save(bookId, commentId, description);
    }

    @Transactional
    @Override
    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private void save(long bookId, long commentId, String description) {
        var bookOpt = bookRepository.findAllById(bookId);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: book '%d' not found".formatted(bookId));
        }
        commentRepository.save(new Comment(commentId, description, bookOpt.get()));
    }

}
