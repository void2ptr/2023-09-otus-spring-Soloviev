package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
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
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BooksController {
    private static final String API_PATH = "/api/v1";

    private final BookService bookService;

    @GetMapping("/book")
    public String listPage(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return API_PATH + "/book/books";
    }

    @GetMapping("/book/0/add") // Без фанатизма
    public String addPage(Model model) {
        long bookId = 0;
        BookDto bookDto = new BookDto(bookId, "New Book", null, null);
        model.addAttribute("book", bookDto);

        List<AuthorDto> authorDtoList = bookService.findAllAuthorsNotInBook(bookId);
        model.addAttribute("authors", authorDtoList);

        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(bookId);
        model.addAttribute("genres", genresDto);
        model.addAttribute("action", "add");
        return API_PATH + "/book/book";
    }

    @GetMapping("/book/{id}/edit")
    public String editPage(@PathVariable("id") Long bookId, Model model) {
        BookDto bookDto = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(bookId)));
        List<AuthorDto> authorsDto = bookService.findAllAuthorsNotInBook(bookId);
        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(bookId);

        model.addAttribute("book", bookDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("action", "edit");
        return API_PATH + "/book/book";
    }

    @GetMapping("/book/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(id)));
        List<AuthorDto> authorsDto = bookService.findAllAuthorsNotInBook(id);
        List<GenreDto> genresDto = bookService.findAllGenresNotInBook(id);

        model.addAttribute("book", bookDto);
        model.addAttribute("genres", genresDto);
        model.addAttribute("authors", authorsDto);
        model.addAttribute("action", "delete");
        return API_PATH + "/book/book";
    }

    @PostMapping("/book/0/add") // Без фанатизма
    public String addAction(
            @Valid @NotNull @FormParam("id") final long id,
            @Valid @NotNull @FormParam("title") final String title,
            @Valid @NotNull @FormParam("authorId") final Long authorId,
            @Valid @NotNull @FormParam("genresId") final Long[] genresId
    ) {
        BookDto bookDto = this.fromForm(id, title, authorId, genresId);
        bookService.insert(
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenres().stream()
                        .map(Genre::getId)
                        .toList()
        );
        return "redirect:%s/book".formatted(API_PATH);
    }

    @PostMapping("/book/{id}/edit")
    public String editAction(
            @PathVariable("id") long id,
            @Valid @NotNull @FormParam("title") final String title,
            @Valid @NotNull @FormParam("authorId") final Long authorId,
            @Valid @NotNull @FormParam("genresId") final Long[] genresId
    ) {
        BookDto bookDto = this.fromForm(id, title, authorId, genresId);
        bookService.update(
                id,
                bookDto.getTitle(),
                bookDto.getAuthor().getId(),
                bookDto.getGenres()
                        .stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList())
        );
        return "redirect:%s/book".formatted(API_PATH);
    }

    @PostMapping("/book/{id}/delete")
    public String deleteAction(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:%s/book".formatted(API_PATH);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private BookDto fromForm(long id, String title, Long authorId, Long[] genresId) {
        Author author = AuthorMapper.toAuthor(bookService.findAuthorsById(authorId));
        List<Genre> genres = Arrays.stream(genresId)
                .map(bookService::findGenresById)
                .map(GenreMapper::toGenre)
                .toList();
        BookDto bookDto = new BookDto(id, title, author, genres);
        if (bookDto.getAuthor() == null) {
            throw new EntityNotFoundException("Author don`t set");
        }
        if (bookDto.getGenres() == null) {
            throw new EntityNotFoundException("Genres don`t set");
        }
        return bookDto;
    }
}
