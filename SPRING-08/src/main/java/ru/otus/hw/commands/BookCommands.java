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

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
                .map(bookConverter::bookToString)
                .orElse("Book with id %s not found".formatted(id));
    }

    // bins "Подарки Второму храму" "Цезарь Гай Юлий,Иосиф Флавий" "хроника,видения,эпос"
    // bins "Падение яблока" "Никколо Макиавелли,Эзоп" "мемуар,комедия"
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, List<String> authorNames, List<String> genresIds) {
        var savedBook = bookService.insert(title, authorNames, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    // bupd "Война и Мир" "Макиавелли,Эсхил" "эпос,военная проза"
    // bupd "Золотой осёл" "Ливий,Аппулей" "скетч,хроника"
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String title, List<String> authorNames, List<String> genresIds) {
        var savedBook = bookService.update(title, authorNames, genresIds);
        return bookConverter.bookToString(savedBook);
    }

    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void updateBook(String id) {
        bookService.deleteById(id);
    }
}
