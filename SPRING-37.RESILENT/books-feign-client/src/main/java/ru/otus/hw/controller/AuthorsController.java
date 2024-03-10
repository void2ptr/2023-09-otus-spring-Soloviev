package ru.otus.hw.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.feign.AuthorsFeignClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorsFeignClient authorsFeignClient;

    @GetMapping("/api/v1/feign/authors")
    @CircuitBreaker(name = "ControllerAuthorFindAll")
    public List<AuthorDto> findAll() {
        return authorsFeignClient.findAll();
    }

    @GetMapping("/api/v1/feign/authors/{id}")
    @CircuitBreaker(name = "ControllerAuthorFindById")
    public AuthorDto findById(@PathVariable("id") Long id) {
        return authorsFeignClient.findById(id);
    }

    @PostMapping("/api/v1/feign/authors")
    @CircuitBreaker(name = "ControllerAuthorInsert")
    public AuthorDto insertAction(@RequestBody AuthorDto authorDto) {
        return authorsFeignClient.insertAction(authorDto);
    }

    @PutMapping("/api/v1/feign/authors")
    @CircuitBreaker(name = "ControllerAuthorsUpdate")
    public AuthorDto updateAction(@RequestBody AuthorDto authorDto) {
        return authorsFeignClient.updateAction(authorDto);
    }

    @DeleteMapping("/api/v1/feign/authors/{id}")
    @CircuitBreaker(name = "ControllerAuthorsDelete")
    public AuthorDto deleteAction(@PathVariable("id") long id) {
        return authorsFeignClient.deleteAction(id);
    }

}
