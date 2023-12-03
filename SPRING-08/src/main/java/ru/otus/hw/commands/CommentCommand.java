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

    // bins "Золотой осёл" "Никколо Макиавелли,Эзоп" "мемуар,комедия"
    // cbid "Золотой осёл"
    @ShellMethod(group = "Comment Command",
            value = "Find Comments by Book title", key = "cbid")
    public String findCommentByBookId(String title) {
        return commentService.findCommentsByBookTitle(title).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // db.books.find( {}, { 'title' : 1, '_id' : 1 } )
    // db.books.find( { 'title' : 'Падение Большого Яблока'}, { 'title' : 1, '_id' : 1 } )
    // bins "Падение Большого Яблока" "Цезарь Гай Юлий,Макиавелли" "мемуар,хроника,военная проза"
    // cins book._id
    @ShellMethod(group = "Comment Command",
            value = "Insert Comments by book _id", key = "cins")
    public String insertComment(String bookId) {
        var insertedComment = commentService.insert(bookId);
        return commentConverter.commentToString(insertedComment);
    }

    // ab
    // db.comments.find( {}, { 'title' : 1, '_id' : 1 } )
    // cdel comments._id
    @ShellMethod(group = "Comment Command",
            value = "Delete Comments by _id", key = "cdel")
    public void deleteComment(String commentId) {
        commentService.delete(commentId);
    }


    // nins "Золотой осёл" "Шедевр античности"
    // nins "Золотой осёл" "Это просто чернуха!"
    @ShellMethod(group = "Comment-Note Command",
            value = "Insert 'Comment Note' by 'Book title'", key = "nins")
    public String insertNote(String title, String comment) {
        var savedComment = commentService.insertNote(title, comment);
        return commentConverter.commentToString(savedComment);
    }

    // db.comments.find( { 'title' : 'Золотой осёл' }, { 'notes' : 1, '_id' : 0 } )
    // nupd "Золотой осёл" 0 "Даже ослы читают это!"
    // nupd "Золотой осёл" 1 "Свежий взгляд на мир )"
    // nupd "Золотой осёл" 100 "Свежий взгляд на мир !!!"
    @ShellMethod(group = "Comment-Note Command",
            value = "Update 'Comment Note' by 'Book title' and 'note id'", key = "nupd")
    public String updateNote(String title, int commentId, String note) {
        var savedComment = commentService.updateNote(title, commentId, note);
        return commentConverter.commentToString(savedComment);
    }

    // ndel "Золотой осёл" 0
    // ndel "Золотой осёл" 1
    @ShellMethod(group = "Comment-Note Command",
            value = "Delete 'Comment Note' by 'Book title' and 'note id'", key = "ndel")
    public String deleteNote(String title, int noteId) {
        var savedComment = commentService.deleteNote(title, noteId);
        return commentConverter.commentToString(savedComment);
    }
}
