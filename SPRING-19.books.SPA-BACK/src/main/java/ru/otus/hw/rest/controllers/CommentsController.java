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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentsController {

    private static final String API_PATH = "/api/v1";

    private final CommentService commentService;

    @GetMapping("/book/{bookId}/comment")
    public String listPage(@PathVariable("bookId") Long bookId, Model model) {
        List<CommentDto> comments = commentService.findCommentByBookId(bookId);
        Optional<CommentDto> commentOpt = comments.stream().findFirst();
        if (commentOpt.isEmpty()) {
            return "redirect:%s/book/%d/comment/0/add".formatted(API_PATH, bookId);
        }

        Book book = commentOpt.orElseThrow(() ->
                        new EntityNotFoundException("Book for Comment with id '%d' not found".formatted(bookId)))
                .getBook();

        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return API_PATH + "/comment/comments";
    }

    @GetMapping("/book/{bookId}/comment/0/add") // без фанатизма
    public String addPage(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDto = new CommentDto(0, "", BookMapper.toBook(bookDto));
        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDto);
        model.addAttribute("action", "add");
        return API_PATH + "/comment/comment";
    }

    @GetMapping("/book/{bookId}/comment/{commentId}/edit")
    public String editPage(@PathVariable("bookId") Long bookId,
                           @PathVariable("commentId") Long commentId,
                           Model model
    ) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDtoByCommentID = commentService.findCommentById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id '%d' not found".formatted(commentId)));

        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDtoByCommentID);
        model.addAttribute("action", "edit");
        return API_PATH + "/comment/comment";
    }

    @GetMapping("/book/{bookId}/comment/{commentId}/delete")
    public String deletePage(@PathVariable("bookId") Long bookId,
                             @PathVariable("commentId") Long commentId,
                             Model model
    ) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDtoByCommentID = commentService.findCommentById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id '%d' not found".formatted(commentId)));

        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDtoByCommentID);
        model.addAttribute("action", "delete");
        return API_PATH + "/comment/comment";
    }

    @PostMapping("/book/{bookId}/comment/0/add") // Без фанатизма
    public String insertAction(@PathVariable("bookId") Long bookId, String description) {
        commentService.insert(bookId,description);
        return "redirect:%s/book/%d/comment".formatted(API_PATH, bookId);
    }

    @PostMapping("/book/{bookId}/comment/{commentId}/edit")
    public String updateAction(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId,
                               String description) {
        commentService.update(bookId, commentId, description);
        return "redirect:%s/book/%d/comment".formatted(API_PATH, bookId);
    }

    @PostMapping("/book/{bookId}/comment/{commentId}/delete")
    public String deleteAction(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return "redirect:%s/book/%d/comment".formatted(API_PATH, bookId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
