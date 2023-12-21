package ru.otus.hw.controllers;

import ch.qos.logback.core.testUtil.MockInitialContext;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/author/{id}/add")
    public String addPage(@PathVariable("id") Long id, Model model) {
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
//    public String addAction(AuthorDto authorDto) {  // app - ok ; test - err
//    public String addAction(@RequestBody AuthorDto authorDto) {  // err app; test - ok
    public String addAction(
            @RequestParam("id")       long id,
            @RequestParam("fullName") String fullName
            )
    {
        AuthorDto authorDto = new AuthorDto(id, fullName);

        // FIXME mock не работает
        // в режиме приложения произойдет вставка записей в ДБ
        // в случае :
        //   given(authorService.insert(authorDto)).willReturn(Optional.of(authorDto));
        // вставки НЕ происходит
        System.out.println("2 при переходе в тестируемый метод: addAction() - mock не работает");
        //System.out.println("2 mock ERR ++ " +  authorService);
        System.out.println("2 mock ERR >> " +  authorService.insert(authorDto)); // 1-я вставка
        System.out.println("2 mock ERR >> " +  authorService.insert(authorDto)); // 2-я вставка
        System.out.println("2 mock ERR >> " +  authorService.insert(authorDto)); // 3-я вставка
        System.out.println("2 mock не работает!!! - вставка НЕ происходит");
        // FIXME mock не работает

        authorService.insert(authorDto).orElseThrow(
                () -> new EntityNotFoundException("ERROR: Author not added !")
        );
        return "redirect:" + API_PATH + "/author";
    }

    @PostMapping("/author/{id}/edit")
    public String updateAction(@PathVariable("id") Long id, AuthorDto authorDto) {
        authorService.update(authorDto).orElseThrow(
                () -> new EntityNotFoundException("Author with id '%d' don't saved".formatted(id))
        );
        return "redirect:" + API_PATH + "/author";
    }

    @PostMapping("/author/{id}/delete")
    public String deleteAction(@PathVariable("id") Long id) {
        if ( ! authorService.delete(id) ) {
            new EntityNotFoundException("Author with id '%d' don't deleted".formatted(id));
        }
        return "redirect:" + API_PATH + "/author";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
