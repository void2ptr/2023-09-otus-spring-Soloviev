package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    // ab
    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // bbid 1
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(long id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    // bins "Новая книга 1" 1 1,6
    // bins "Новая книга 2" 2 3,5
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, long authorId, List<Long> genresIds) {
        var savedBook = bookService.insert(title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    // bupd 4 "Новое название" 3 2,5
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(long id, String title, long authorId, List<Long> genresIds) {
        var savedBook = bookService.update(id, title, authorId, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    // bdel 2
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void updateBook(long id) {
        bookService.deleteById(id);
    }
}
