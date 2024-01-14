package ru.otus.hw.controller;

import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookIdsDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "/books/books";
    }

    @GetMapping("/books/add")
    public String addPage(Model model) {
        long bookId = 0;
        BookDto bookDto = new BookDto(bookId, "New Book", null, null);
        List<AuthorDto> authorsDto = authorService.findAuthorsNotInBook(bookId);
        List<GenreDto> genresDto = genreService.findGenresNotInBook(bookId);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        return "/books/book-add";
    }

    @GetMapping("/books/{id}/edit")
    public String editPage(@PathVariable("id") long bookId, Model model) {
        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(bookId)));
        List<AuthorDto> authorsDto = authorService.findAuthorsNotInBook(bookId);
        List<GenreDto> genresDto = genreService.findGenresNotInBook(bookId);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        return "/books/book-edit";
    }

    @GetMapping("/books/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(id)));
        List<AuthorDto> authorsDto = authorService.findAuthorsNotInBook(id);
        List<GenreDto> genresDto = genreService.findGenresNotInBook(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("authors", authorsDto);
        return "/books/book-delete";
    }

    @PostMapping("/books/add")
    public String insert(BookIdsDto bookIdsDto) {
        bookService.insert(bookIdsDto);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/edit")
    public String update(BookIdsDto bookIdsDto) {
        bookService.update(bookIdsDto);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

}
