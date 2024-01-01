package ru.otus.hw.rest;

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


@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @PostMapping("/api/v1/books") // Без фанатизма
    public BookDto addAction(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PutMapping("/api/v1/books/{id}")
    public BookDto editAction(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public BookDto deleteAction(@PathVariable("id") long id) {
        return bookService.delete(id);
    }
}
