package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
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
    public List<Comment> findCommentByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(long bookId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("ERROR: book %s not found".formatted(bookId));
        }
        var comment = new Comment(0, text, book.get());
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(long bookId, long commentId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("ERROR: book %s not found".formatted(bookId));
        }
        var comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment %s not found".formatted(commentId));
        }
        return commentRepository.save(new Comment(commentId, text, book.get()));
    }

    @Transactional
    @Override
    public void delete(long commentId) {
        var comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

}
