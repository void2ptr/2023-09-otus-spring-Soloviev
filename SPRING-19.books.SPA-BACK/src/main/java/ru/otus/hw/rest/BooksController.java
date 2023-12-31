package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BooksController {

    private final BookService bookService;

    @GetMapping("/book")
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @PostMapping("/book/add") // Без фанатизма
    public BookDto addAction(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PostMapping("/book/{id}/edit")
    public BookDto editAction(@PathVariable("id") long id, @RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/book/{id}/delete")
    public BookDto deleteAction(@PathVariable("id") long id) {
        return bookService.delete(id);
    }
}
