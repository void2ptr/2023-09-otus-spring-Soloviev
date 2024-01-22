package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String listPage(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "/authors/authors";
    }

    @GetMapping("/authors/add")
    public String addPage(Model model) {
        AuthorDto author = new AuthorDto(0, "New Author");
        model.addAttribute("author", author);
        return "/authors/author-add";
    }

    @GetMapping("/authors/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "/authors/author-edit";
    }

    @GetMapping("/authors/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        return "/authors/author-delete";
    }

    @PostMapping("/authors/add")
    public String insert(String fullName) {
        authorService.insert(new AuthorDto(0, fullName));
        return "redirect:/authors";
    }

    @PostMapping("/authors/{id}/edit")
    public String update(@PathVariable("id") Long id, String fullName) {
        authorService.update(new AuthorDto(id, fullName));
        return "redirect:/authors";
    }

    @PostMapping("/authors/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!authorService.delete(id)) {
            throw new EntityNotFoundException("Author with id '%d' don't deleted".formatted(id));
        }
        return "redirect:/authors";
    }

}
