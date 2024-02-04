package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/genres")
    public String listPage(Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "/genres/genres";
    }

    @GetMapping("/genres/add")
    public String addPage(Model model) {
        GenreDto genre = new GenreDto(0, "new Genre");
        model.addAttribute("genre", genre);
        return "/genres/genre-add";
    }

    @GetMapping("/genres/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        return "/genres/genre-edit";
    }

    @GetMapping("/genres/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        GenreDto genre = genreService.findGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id '%d' not found".formatted(id)));
        model.addAttribute("genre", genre);
        return "/genres/genre-delete";
    }

    @PostMapping("/genres/add")
    public String insert(GenreDto genreDto) {
        genreService.insert(genreDto);
        return "redirect:/genres";
    }

    @PostMapping("/genres/{genreId}/edit")
    public String update(GenreDto genreDto) {
        genreService.update(genreDto);
        return "redirect:/genres";
    }

    @PostMapping("/genres/{genreId}/delete")
    public String delete(@PathVariable("genreId") Long genreId) {
        genreService.delete(genreId);
        return "redirect:/genres";
    }

}
