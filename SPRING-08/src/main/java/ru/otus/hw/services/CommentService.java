package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findCommentsByBookTitle(String title);

    Comment insert(String bookId);

    void delete(String commentId);

    Comment insertNote(String title, String note);

    Comment updateNote(String title, int commentId, String note);

    Comment deleteNote(String title, int noteId);

}
