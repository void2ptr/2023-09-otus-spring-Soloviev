package ru.otus.hw.services;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    @Transactional(readOnly = true)
    List<Comment> findCommentsByBookTitle(String title);

    @Transactional
    Comment insert(String bookId);

    @Transactional
    void delete(String commentId);

    @Transactional
    Comment insertNote(String title, String note);

    @Transactional
    Comment updateNote(String title, int commentId, String note);

    @Transactional
    Comment deleteNote(String title, int noteId);

}
