package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
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
        return commentRepository.findCommentsByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(long bookId, String text) {
        var book = bookRepository.findBookById(bookId);
        if (book.isEmpty()) {
            return new Comment(0, "ERROR: book not found", new Book());
        }
        var comment = new Comment(0, text, book.get());
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(long bookId, long commentId, String text) {
        var book = bookRepository.findBookById(bookId);
        if (book.isEmpty()) {
            return new Comment(commentId, "ERROR: book not found", new Book());
        }
        var comment = commentRepository.findCommentById(commentId);
        if (comment.isEmpty()) {
            return new Comment(commentId, "ERROR: comment not found", new Book());
        }
        return commentRepository.save(new Comment(commentId, text, book.get()));
    }

    @Transactional
    @Override
    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

}