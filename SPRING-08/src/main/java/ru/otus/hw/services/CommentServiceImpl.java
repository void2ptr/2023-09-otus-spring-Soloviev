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
    public List<Comment> findCommentByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Transactional
    @Override
    public Comment insert(String bookId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return new Comment("0", new Book(), "ERROR: book not found");
        }
        var comment = new Comment("0", book.get(), text);
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(String bookId, String commentId, String text) {
        var book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            return new Comment(commentId, new Book(), "ERROR: book not found");
        }
        var comment = commentRepository.findById(commentId);
        if (comment.isEmpty()) {
            return new Comment(commentId, new Book(), "ERROR: comment not found");
        }
        return commentRepository.save(new Comment(commentId, book.get(), text));
    }

    @Transactional
    @Override
    public void delete(String commentId) {
        commentRepository.deleteById(commentId);
    }

}
