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
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.service.AuthorService;


@RestController
@RequiredArgsConstructor
public class AuthorsController {
    private final AuthorService authorService;

//    @GetMapping(value = "/api/v1/authors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    @GetMapping("/api/v1/authors")
    @ResponseStatus(HttpStatus.OK)
    public Flux<AuthorDto> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthorDto> findById(@PathVariable("id") Long id) {
        return authorService.findById(id);
    }

    @PostMapping("/api/v1/authors")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorDto> insert(@RequestBody AuthorDto authorDto) {
        return authorService.insert(authorDto);
    }

    @PutMapping("/api/v1/authors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<AuthorDto> update(@PathVariable("id") String id, @RequestBody AuthorDto authorDto) {
        return authorService.update(authorDto);
    }

    @DeleteMapping("/api/v1/authors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> delete(@PathVariable("id") Long id) {
        return authorService.delete(id);
    }
}
