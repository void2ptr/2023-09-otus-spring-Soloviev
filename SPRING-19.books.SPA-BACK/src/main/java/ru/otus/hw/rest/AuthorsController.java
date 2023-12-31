package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/author")
    public List<AuthorDto> findAll() {
        return authorService.findAll().stream()
                .map(AuthorMapper::toDto)
                .toList();
    }

    @GetMapping("/author/{id}")
    public AuthorDto findById(@PathVariable("id") Long id) {
        return AuthorMapper.toDto(authorService.findAuthorById(id));
    }

    @PostMapping("/author/add")
    public AuthorDto addAction(@RequestBody AuthorDto authorDto) {
        return AuthorMapper.toDto(authorService.insert(AuthorMapper.toAuthor(authorDto)));
    }

    @PostMapping("/author/{id}/edit")
    public AuthorDto updateAction(@PathVariable("id") long id, @RequestBody AuthorDto authorDto) {
        return AuthorMapper.toDto(authorService.update(AuthorMapper.toAuthor(authorDto)));
    }

    @DeleteMapping("/author/{id}/delete")
    public AuthorDto deleteAction(@PathVariable("id") long id) {
        return AuthorMapper.toDto(authorService.delete(id));
    }
}
