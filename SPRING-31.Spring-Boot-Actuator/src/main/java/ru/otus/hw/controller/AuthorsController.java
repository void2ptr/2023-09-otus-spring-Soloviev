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
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDto> findAll() {
        return authorService.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @GetMapping("/api/v1/authors/{id}")
    public AuthorDto findById(@PathVariable("id") Long id) {
        return AuthorMapper.toDto(authorService.findAuthorById(id));
    }

    @PostMapping("/api/v1/authors")
    public AuthorDto addAction(@RequestBody AuthorDto authorDto) {
        return AuthorMapper.toDto(authorService.insert(AuthorMapper.toAuthor(authorDto)));
    }

    @PutMapping("/api/v1/authors/{id}")
    public AuthorDto updateAction(@PathVariable("id") long id, @RequestBody AuthorDto authorDto) {
        return AuthorMapper.toDto(authorService.update(AuthorMapper.toAuthor(authorDto)));
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public AuthorDto deleteAction(@PathVariable("id") long id) {
        return AuthorMapper.toDto(authorService.delete(id));
    }
}
