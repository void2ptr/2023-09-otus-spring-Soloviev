package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
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
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> findAll() {
        return genreService.findAll();
    }

    @PostMapping("/api/v1/genres")
    public GenreDto insertAction(@RequestBody GenreDto genreDto) {
        return genreService.insert(genreDto);
    }

    @PutMapping("/api/v1/genres")
    public GenreDto updateAction(@RequestBody GenreDto genreDto) {
        return genreService.update(genreDto);
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public GenreDto deleteAction(@PathVariable("id") long id) {
        return genreService.delete(id);
    }

}
