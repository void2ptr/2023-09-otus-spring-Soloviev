package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.service.BookService;

@RestController
@RequiredArgsConstructor
public class BooksController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.OK)
    public Flux<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> findById(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        return bookService.findById(bookDto);
    }

    @PostMapping("/api/v1/books")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> insert(@RequestBody BookDto bookDto) {
        return bookService.insert(bookDto);
    }

    @PutMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> update(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookDto> delete(@PathVariable("id") Long id) {
        return bookService.delete(id);
    }
}
