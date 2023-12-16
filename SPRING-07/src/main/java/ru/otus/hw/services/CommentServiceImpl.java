package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long bookId) {
        return commentRepository.findCommentsByBookId(bookId).stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto insert(long bookId, String text) {
        return this.save(bookId, 0, text);
    }

    @Transactional
    @Override
    public CommentDto update(long bookId, long commentId, String text) {
        var comment = commentRepository.findCommentById(commentId);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentId));
        }
        return this.save(bookId, commentId, text);
    }

    @Transactional
    @Override
    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    private CommentDto save(long bookId, long commentId, String description) {
        var bookOpt = bookRepository.findBookById(bookId);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: book '%d' not found".formatted(bookId));
        }
        return CommentMapper.toDto(commentRepository.save(new Comment(commentId, description, bookOpt.get())));
    }

}
