package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommand {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // cbid 1
    @ShellMethod(value = "Find Comments by Book id", key = "cbid")
    public String findCommentByBookId(String bookId) {
        return commentService.findCommentByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins 1 comment_4
    // cins 1 comment_5
    @ShellMethod(value = "Insert Comments by Book id", key = "cins")
    public String insertComment(String bookId, String comment) {
        var savedBook = commentService.insert(bookId, comment);
        return commentConverter.commentToString(savedBook);
    }

    // cupd 1 1 comment_5
    @ShellMethod(value = "Update Comments by Book and Comment id", key = "cupd")
    public String updateComment(String bookId, String commentId, String comment) {
        var savedBook = commentService.update(bookId, commentId, comment);
        return commentConverter.commentToString(savedBook);
    }

    // cdel 3
    @ShellMethod(value = "Delete Comments by id", key = "cdel")
    public void deleteComment(String commentId) {
        commentService.delete(commentId);
    }


}
