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
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getAll() {
        return genreService.findAll()
                .stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/api/v1/genres")
    public GenreDto addAction(@RequestBody GenreDto genreDto) {
        return GenreMapper.toDto(genreService.insert(GenreMapper.toGenre(genreDto)));
    }

    @PutMapping("/api/v1/genres/{id}")
    public GenreDto updateAction(@PathVariable("id") long id, @RequestBody GenreDto genreDto) {
        return GenreMapper.toDto(genreService.update(GenreMapper.toGenre(genreDto)));
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public GenreDto deleteAction(@PathVariable("id") long id) {
        return GenreMapper.toDto(genreService.delete(id));
    }

}
