package ru.otus.hw.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthorsController {
    private static final String API_PATH = "/api/v1";

    private final AuthorService authorService;

    @GetMapping("/author")
    public String listPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return API_PATH + "/author/authors";
    }

    @GetMapping("/author/0/add") // Без фанатизма
    public String addPage(Model model) {
        AuthorDto author = new AuthorDto(0, "New Author");
        model.addAttribute("author", author);
        model.addAttribute("action", "add");
        return API_PATH + "/author/author";
    }

    @GetMapping("/author/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "edit");
        return API_PATH + "/author/author";
    }

    @GetMapping("/author/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "delete");
        return API_PATH + "/author/author";
    }

    @PostMapping("/author/{id}/add")
    public String addAction(@PathVariable("id") Long id, String fullName) {
        authorService.insert(new AuthorDto(id, fullName));
        return "redirect:" + API_PATH + "/author";
    }

    @PostMapping("/author/{id}/edit")
    public String updateAction(@PathVariable("id") Long id, String fullName) {
        authorService.update(new AuthorDto(id, fullName));
        return "redirect:" + API_PATH + "/author";
    }

    @PostMapping("/author/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        if (!authorService.delete(id)) {
            throw new EntityNotFoundException("Author with id '%d' don't deleted".formatted(id));
        }
        return "redirect:" + API_PATH + "/author";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
