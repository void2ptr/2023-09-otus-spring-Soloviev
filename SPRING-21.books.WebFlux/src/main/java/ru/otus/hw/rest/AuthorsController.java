package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.service.AuthorService;


@RestController
@RequiredArgsConstructor
public class AuthorsController {
    private final AuthorService authorService;

//    @GetMapping(value = "/api/v1/authors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @GetMapping("/api/v1/authors")
    public Flux<AuthorDto> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    public Mono<AuthorDto> findById(@PathVariable("id") Long id) {
        return authorService.findAuthorById(id);
    }

    @PostMapping("/api/v1/authors")
    public Mono<AuthorDto> addAction(@RequestBody AuthorDto authorDto) {
        return authorService.insert(AuthorMapper.toAuthor(authorDto));
    }

    @PutMapping("/api/v1/authors/{id}")
    public Mono<AuthorDto> updateAction(@PathVariable("id") String id, @RequestBody AuthorDto authorDto) {
        return authorService.update(AuthorMapper.toAuthor(authorDto));
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public Mono<AuthorDto> deleteAction(@PathVariable("id") Long id) {
        return authorService.delete(id);
    }
}
