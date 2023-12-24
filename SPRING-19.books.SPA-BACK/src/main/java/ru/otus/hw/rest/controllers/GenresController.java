package ru.otus.hw.rest.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GenresController {
    private static final String API_PATH = "/api/v1";

    private final GenreService genreService;

    @GetMapping("/genre")
    public String listPage(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return API_PATH + "/genre/genres";
    }

    @GetMapping("/genre/0/add")
    public String addPage(Model model) {
        GenreDto genre = new GenreDto(0, "new Genre");
        model.addAttribute("genre", genre);
        model.addAttribute("action", "add");
        return API_PATH + "/genre/genre";
    }

    @GetMapping("/genre/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        model.addAttribute("action", "edit");
        return API_PATH + "/genre/genre";
    }

    @GetMapping("/genre/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        model.addAttribute("action", "delete");
        return API_PATH + "/genre/genre";
    }

    @PostMapping("/genre/0/add") // Без фанатизма
    public String addAction(GenreDto genreDto) {
        genreService.insert(genreDto);
        return "redirect:" + API_PATH + "/genre";
    }

    @PostMapping("/genre/{genreId}/edit")
    public String updateAction(GenreDto genreDto) {
        genreService.update(genreDto);
        return "redirect:" + API_PATH + "/genre";
    }

    @PostMapping("/genre/{genreId}/delete")
    public String deleteAction(@PathVariable("genreId") Long genreId) {
        genreService.delete(genreId);
        return "redirect:" + API_PATH + "/genre";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
