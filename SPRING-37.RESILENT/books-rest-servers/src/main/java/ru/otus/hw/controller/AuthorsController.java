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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    @CircuitBreaker(name = "ControllerAuthorFindAll", fallbackMethod = "recoverFindAll")
    public List<AuthorDto> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    @CircuitBreaker(name = "ControllerAuthorFindById", fallbackMethod = "recoverFindById")
    public AuthorDto findById(@PathVariable("id") Long id) {
        return authorService.findAuthorById(id);
    }

    @PostMapping("/api/v1/authors")
    @CircuitBreaker(name = "ControllerAuthorInsert", fallbackMethod = "recoverInsertAction")
    public AuthorDto insertAction(@RequestBody AuthorDto authorDto) {
        return authorService.insert(authorDto);
    }

    @PutMapping("/api/v1/authors")
    @CircuitBreaker(name = "ControllerAuthorsUpdate", fallbackMethod = "recoverUpdateAction")
    public AuthorDto updateAction(@RequestBody AuthorDto authorDto) {
        return authorService.update(authorDto);
    }

    @DeleteMapping("/api/v1/authors/{id}")
    @CircuitBreaker(name = "ControllerAuthorsDelete", fallbackMethod = "recoverDeleteAction")
    public AuthorDto deleteAction(@PathVariable("id") long id) {
        return authorService.delete(id);
    }

    private List<AuthorDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);

        return authorService.findAll();
    }

    public AuthorDto recoverFindById(Long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return authorService.findAuthorById(id);
    }

    public AuthorDto recoverInsertAction(AuthorDto authorDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return authorService.insert(authorDto);
    }

    public AuthorDto recoverUpdateAction(AuthorDto authorDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return authorService.update(authorDto);
    }

    public AuthorDto recoverDeleteAction(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return authorService.delete(id);
    }

}
