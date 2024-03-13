package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.service.BookService;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Optional<BookDto> findById(@PathVariable("id") long id) {
        return bookService.findById(id);
    }

    @PostMapping("/api/v1/books")
    public BookDto insertAction(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PutMapping("/api/v1/books")
    public BookDto updateAction(@RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public BookDto deleteAction(@PathVariable("id") long id) {
        return bookService.delete(id);
    }

}
