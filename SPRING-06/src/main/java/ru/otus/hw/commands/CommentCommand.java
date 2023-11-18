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

    @ShellMethod(value = "Find Comments by Book id", key = "cbid")
    public String findCommentByBookId(long id) {
        return commentService.findCommentByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

//    @ShellMethod(value = "Insert comment", key = "cins")
//    public String insertBook(String title, long authorId, List<Long> genresIds) {
//        var savedBook = bookService.insert(title, authorId, genresIds);
//        return commentConverter.commentToString(savedBook);
//    }
}
