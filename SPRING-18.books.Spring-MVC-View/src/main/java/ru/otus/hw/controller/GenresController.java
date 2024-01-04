package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public String listPage(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "/api/v1/genre/genres";
    }

    @GetMapping("/api/v1/genres/add")
    public String addPage(Model model) {
        GenreDto genre = new GenreDto(0, "new Genre");
        model.addAttribute("genre", genre);
        return "/api/v1/genre/genre-add";
    }

    @GetMapping("/api/v1/genres/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        return "/api/v1/genre/genre-edit";
    }

    @GetMapping("/api/v1/genres/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        return "/api/v1/genre/genre-delete";
    }

    @PostMapping("/api/v1/genres/add")
    public String addAction(GenreDto genreDto) {
        genreService.insert(genreDto);
        return "redirect:/api/v1/genres";
    }

    @PostMapping("/api/v1/genres/{genreId}/edit")
    public String updateAction(GenreDto genreDto) {
        genreService.update(genreDto);
        return "redirect:/api/v1/genres";
    }

    @PostMapping("/api/v1/genres/{genreId}/delete")
    public String deleteAction(@PathVariable("genreId") Long genreId) {
        genreService.delete(genreId);
        return "redirect:/api/v1/genres";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
