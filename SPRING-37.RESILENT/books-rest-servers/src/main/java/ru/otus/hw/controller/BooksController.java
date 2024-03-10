package ru.otus.hw.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BooksController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    @CircuitBreaker(name = "ControllerBooksFindAll", fallbackMethod = "recoverFindAll")
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @PostMapping("/api/v1/books")
    @CircuitBreaker(name = "ControllerBooksInsert", fallbackMethod = "recoverInsertAction")
    public BookDto insertAction(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PutMapping("/api/v1/books")
    @CircuitBreaker(name = "ControllerBooksUpdate", fallbackMethod = "recoverUpdateAction")
    public BookDto updateAction(@RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @CircuitBreaker(name = "ControllerBooksDelete", fallbackMethod = "recoverDeleteAction")
    public BookDto deleteAction(@PathVariable("id") long id) {
        return bookService.delete(id);
    }


    private List<BookDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return bookService.findAll();
    }

    private BookDto recoverInsertAction(BookDto bookDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return bookService.insert(bookDto);
    }

    private BookDto recoverUpdateAction(BookDto bookDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return bookService.update(bookDto);
    }

    public BookDto recoverDeleteAction(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return bookService.delete(id);
    }

}
