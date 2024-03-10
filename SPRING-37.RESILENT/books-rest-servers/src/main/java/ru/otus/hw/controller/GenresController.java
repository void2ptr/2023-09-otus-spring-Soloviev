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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    @CircuitBreaker(name = "ControllerGenresFindAll", fallbackMethod = "recoverFindAll")
    public List<GenreDto> findAll() {
        return genreService.findAll();
    }

    @PostMapping("/api/v1/genres")
    @CircuitBreaker(name = "ControllerGenresInsert", fallbackMethod = "recoverInsertAction")
    public GenreDto insertAction(@RequestBody GenreDto genreDto) {
        return genreService.insert(genreDto);
    }

    @PutMapping("/api/v1/genres")
    @CircuitBreaker(name = "ControllerGenresUpdate", fallbackMethod = "recoverUpdateAction")
    public GenreDto updateAction(@RequestBody GenreDto genreDto) {
        return genreService.update(genreDto);
    }

    @DeleteMapping("/api/v1/genres/{id}")
    @CircuitBreaker(name = "ControllerGenresDelete", fallbackMethod = "recoverDeleteAction")
    public GenreDto deleteAction(@PathVariable("id") long id) {
        return genreService.delete(id);
    }

    private List<GenreDto> recoverFindAll(Exception ex) {
        log.warn(ex.getMessage(), ex);
        return genreService.findAll();
    }

    private GenreDto recoverInsertAction(GenreDto genreDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return genreService.insert(genreDto);
    }

    private GenreDto recoverUpdateAction(GenreDto genreDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return genreService.update(genreDto);
    }

    private GenreDto recoverDeleteAction(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return genreService.delete(id);
    }

}
