package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
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
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    public AuthorDto findById(@PathVariable("id") Long id) {
        return authorService.findAuthorById(id);
    }

    @PostMapping("/api/v1/authors")
    public AuthorDto insertAction(@RequestBody AuthorDto authorDto) {
        return authorService.insert(authorDto);
    }

    @PutMapping("/api/v1/authors")
    public AuthorDto updateAction(@RequestBody AuthorDto authorDto) {
        return authorService.update(authorDto);
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public AuthorDto deleteAction(@PathVariable("id") long id) {
        return authorService.delete(id);
    }

}
