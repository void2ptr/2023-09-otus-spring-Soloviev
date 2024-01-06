package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDto> getAll() {
        return genreService.findAll();
    }

    @PostMapping("/api/v1/genres")
    public Mono<GenreDto> addAction(@RequestBody GenreDto genreDto) {
        return genreService.insert(GenreMapper.toGenre(genreDto));
    }

    @PutMapping("/api/v1/genres/{id}")
    public Mono<GenreDto> updateAction(@PathVariable("id") Long id, @RequestBody GenreDto genreDto) {
        return genreService.update(GenreMapper.toGenre(genreDto));
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public Mono<GenreDto> deleteAction(@PathVariable("id") Long id) {
        return genreService.delete(id);
    }

}
