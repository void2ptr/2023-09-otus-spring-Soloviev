package ru.otus.hw.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/author")
    public String listPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "/api/v1/author/authors";
    }

    @GetMapping("/api/v1/author/page-add")
    public String addPage(Model model) {
        AuthorDto author = new AuthorDto(0, "New Author");
        model.addAttribute("author", author);
        return "/api/v1/author/author-add";
    }

    @GetMapping("/api/v1/author/{id}/page-edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "/api/v1/author/author-edit";
    }

    @GetMapping("/api/v1/author/{id}/page-delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "/api/v1/author/author-delete";
    }

    @PostMapping("/api/v1/author/add")
    public String addAction(String fullName) {
        authorService.insert(new AuthorDto(0, fullName));
        return "redirect:/api/v1/author";
    }

    @PostMapping("/api/v1/author/{id}/edit")
    public String updateAction(@PathVariable("id") Long id, String fullName) {
        authorService.update(new AuthorDto(id, fullName));
        return "redirect:/api/v1/author";
    }

    @PostMapping("/api/v1/author/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        if (!authorService.delete(id)) {
            throw new EntityNotFoundException("Author with id '%d' don't deleted".formatted(id));
        }
        return "redirect:/api/v1/author";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
