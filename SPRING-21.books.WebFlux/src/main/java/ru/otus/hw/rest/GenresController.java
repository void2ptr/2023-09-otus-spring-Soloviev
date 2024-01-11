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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GenreDto> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/api/v1/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GenreDto> findById(@PathVariable("id") Long id) {
        return genreService.findById(id);
    }

    @PostMapping("/api/v1/genres")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GenreDto> insert(@RequestBody GenreDto genreDto) {
        return genreService.insert(genreDto);
    }

    @PutMapping("/api/v1/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GenreDto> update(@PathVariable("id") Long id, @RequestBody GenreDto genreDto) {
        return genreService.update(genreDto);
    }

    @DeleteMapping("/api/v1/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<GenreDto> delete(@PathVariable("id") Long id) {
        return genreService.delete(id);
    }

}
