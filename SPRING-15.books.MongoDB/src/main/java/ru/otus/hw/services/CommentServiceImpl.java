package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.EntityTooManyException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findCommentsByBookTitle(String title) {
        return commentRepository.findByBookTitle(title);
    }

    @Transactional
    @Override
    public Comment insert(String bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new EntityNotFoundException("Error: Book id '%s' not found"
                    .formatted(bookId));
        }
        return commentRepository.save(new Comment(book.get(), book.get().getTitle()));
    }

    @Transactional
    @Override
    public void delete(String commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public Comment insertNote(String title, String note) {
        Book book = findBookByTitle(title);
        // find Comment by book.id
        Comment comment;
        Optional<Comment> comments = commentRepository.findByBookId(book.getId());
        if (comments.isEmpty()) { // add Comment with Note
            comment = new Comment(book, note);
        } else { // add Note to Comment
            comment = comments.get();
            comment.addNote(note);
        }
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment updateNote(String title, int commentId, String note) {
        Comment comment = findCommentByBookTitle(title);
        comment.updateNote(commentId, note);

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment deleteNote(String title, int noteId) {
        Comment comment = findCommentByBookTitle(title);
        comment.deleteNote(noteId);

        return commentRepository.save(comment);
    }

    private Comment findCommentByBookTitle(String title) {
        List<Comment> comments = commentRepository.findByBookTitle(title);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("Error: Comment for Book with the title '%s' not found"
                    .formatted(title));
        } else if (comments.size() > 1) {
            throw new EntityTooManyException("Error: Many Comments for Book with the title '%s' were found."
                    .formatted(title));
        }
        return comments.get(0);
    }

    private Book findBookByTitle(String title) {
        List<Book> books = bookRepository.findAllByTitleIs(title);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Error: Books with the title '%s' were found.".formatted(title));
        } else if (books.size() > 1) {
            throw new EntityTooManyException("Error: Many Books with the title '%s' were found.".formatted(title));
        }
        return books.get(0);
    }

}
