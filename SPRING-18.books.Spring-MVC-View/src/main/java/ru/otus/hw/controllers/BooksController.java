package ru.otus.hw.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
public class BooksController {
    private static final String API_PATH = "/api/v1/book";

    private final BookService bookService;

    @GetMapping("")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return API_PATH + "/books";
    }

    @GetMapping("/0/add")
    public String addPage(Model model) {
        BookDto bookDto = new BookDto(0,
                "new book",
                null,
                null
        );
        model.addAttribute("book", bookDto);

        List<AuthorDto> authorsDto = bookService.findAllAuthorsNotInBook(0);
        model.addAttribute("authors", authorsDto);

        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(0);
        model.addAttribute("genres", genresDto);
        model.addAttribute("action", "add");
        return API_PATH + "/book";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(id)));
        List<AuthorDto> authorsDto = bookService.findAllAuthorsNotInBook(id);
        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("action", "edit");
        return API_PATH + "/book";
    }

    @GetMapping("/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(id)));
        List<AuthorDto> authorsDto = bookService.findAllAuthorsNotInBook(id);
        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("action", "delete");
        return API_PATH + "/book";
    }

    @PostMapping("/0/add")
    public String addAction(BookDto bookDto) {
        if (bookDto.getAuthor() == null) {
            throw new EntityNotFoundException("Author don`t set");
        }
        if (bookDto.getGenres() == null) {
            throw new EntityNotFoundException("Genres don`t set");
        }
        bookService.insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenres().stream()
                        .map(Genre::getId)
                        .toList()
        );
        return "redirect:" + API_PATH;
    }

    @PostMapping("/{id}/edit")
    public String editAction(BookDto bookDto) {
        if (bookDto.getAuthor() == null) {
            throw new EntityNotFoundException("Author don`t set");
        }
        if (bookDto.getGenres() == null) {
            throw new EntityNotFoundException("Genres don`t set");
        }
        bookService.update(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList())
        );
        return "redirect:" + API_PATH;
    }

    @PostMapping("/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return "redirect:" + API_PATH;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
