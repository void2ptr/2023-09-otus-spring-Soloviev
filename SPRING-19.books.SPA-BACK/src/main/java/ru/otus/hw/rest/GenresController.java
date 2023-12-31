package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/genre")
    public List<GenreDto> getAll() {
        return genreService.findAll()
                .stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/genre/add")
    public GenreDto addAction(@RequestBody GenreDto genreDto) {
        return GenreMapper.toDto(genreService.insert(GenreMapper.toGenre(genreDto)));
    }

    @PostMapping("/genre/{id}/edit")
    public GenreDto updateAction(@PathVariable("id") long id, @RequestBody GenreDto genreDto) {
        return GenreMapper.toDto(genreService.update(GenreMapper.toGenre(genreDto)));
    }

    @DeleteMapping("/genre/{id}/delete")
    public GenreDto deleteAction(@PathVariable("id") long id) {
        return GenreMapper.toDto(genreService.delete(id));
    }

}
