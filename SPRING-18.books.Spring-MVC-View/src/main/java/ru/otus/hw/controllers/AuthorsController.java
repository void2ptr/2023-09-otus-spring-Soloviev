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
@RequestMapping("/api/v1/author")
public class AuthorsController {
    private static final String API_PATH = "/api/v1/author";

    private final AuthorService authorService;

    @GetMapping("")
    public String listPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return API_PATH + "/authors";
    }

    @GetMapping("/0/add")
    public String addPage(Model model) {
        AuthorDto author = new AuthorDto(0, "New Author");
        model.addAttribute("author", author);
        model.addAttribute("action", "add");
        return API_PATH + "/author";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "edit");
        return API_PATH + "/author";
    }

    @GetMapping("/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "delete");
        return API_PATH + "/author";
    }

    @PostMapping("/{id}/add")
    public String addAction(AuthorDto authorDto) {
        authorService.insert(authorDto);
        return "redirect:" + API_PATH;
    }

    @PostMapping("/{id}/edit")
    public String updateAction(AuthorDto authorDto) {
        authorService.update(authorDto);
        return "redirect:" + API_PATH;
    }

    @PostMapping("/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        authorService.delete(id);
        return "redirect:" + API_PATH;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
