package ru.otus.hw.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(
        // Access-Control-Allow-Origin
        origins = { "*" },

        // Alternative to origins that supports more flexible originpatterns.
        // Please, see CorsConfiguration.setAllowedOriginPatterns(List)for details.
        // originPatterns = { "" },

//            // Access-Control-Allow-Credentials
//            allowCredentials = "false",
//
//            // Access-Control-Allow-Headers
//            allowedHeaders = { "*" },
//
//            // Access-Control-Expose-Headers
//            exposedHeaders = { "*" },
//
//            // Access-Control-Max-Age
//            maxAge = 60 * 30,
//
        // Access-Control-Allow-Methods
        methods = {RequestMethod.GET}
)
public class AuthorsController {

    private final AuthorService authorService;

    @GetMapping("/author")
    public List<AuthorDto> listPage(Model model) {
        return authorService.findAll();
    }

    @GetMapping("/author/0/add") // Без фанатизма
    public String addPage(Model model) {
        AuthorDto author = new AuthorDto(0, "New Author");
        model.addAttribute("author", author);
        model.addAttribute("action", "add");
        return "/author/author";
    }

    @GetMapping("/author/{id}/edit")
    public String editPage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "edit");
        return "/author/author";
    }

    @GetMapping("/author/{id}/delete")
    public String deletePage(@PathVariable("id") Long id, Model model) {
        AuthorDto author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        model.addAttribute("action", "delete");
        return "/author/author";
    }

    @PostMapping("/author/{id}/add")
    public String addAction(@PathVariable("id") Long id, String fullName) {
        authorService.insert(new AuthorDto(id, fullName));
        return "redirect:/author";
    }

    @PostMapping("/author/{id}/edit")
    public String updateAction(@PathVariable("id") Long id, String fullName) {
        authorService.update(new AuthorDto(id, fullName));
        return "redirect:/author";
    }

    @PostMapping("/author/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        if (!authorService.delete(id)) {
            throw new EntityNotFoundException("Author with id '%d' don't deleted".formatted(id));
        }
        return "redirect:/author";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
