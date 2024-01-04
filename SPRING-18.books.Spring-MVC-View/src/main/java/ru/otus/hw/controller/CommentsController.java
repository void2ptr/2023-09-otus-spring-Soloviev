package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.CommentService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public String listPage(@PathVariable("bookId") long bookId, Model model) {
        List<CommentDto> commentsDto = commentService.findCommentByBookId(bookId);
        Optional<CommentDto> commentOpt = commentsDto.stream().findFirst();
        if (commentOpt.isEmpty()) {
            return "redirect:/api/v1/books/%d/comments/add".formatted(bookId);
        }

        BookDto bookDto = commentOpt.orElseThrow(() ->
                        new EntityNotFoundException("Book for Comment with id '%d' not found".formatted(bookId)))
                .getBook();

        model.addAttribute("book", bookDto);
        model.addAttribute("comments", commentsDto);
        return "/api/v1/comment/comments";
    }

    @GetMapping("/api/v1/books/{bookId}/comments/add")
    public String addPage(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDto = new CommentDto(0, "", bookDto);
        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDto);
        return "/api/v1/comment/comment-add";
    }

    @GetMapping("/api/v1/books/{bookId}/comments/{commentId}/edit")
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
        return "/api/v1/comment/comment-edit";
    }

    @GetMapping("/api/v1/books/{bookId}/comments/{commentId}/delete")
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
        return "/api/v1/comment/comment-delete";
    }

    @PostMapping("/api/v1/books/{bookId}/comments/add")
    public String insertAction(@PathVariable("bookId") Long bookId, String description) {
        commentService.insert(bookId,description);
        return "redirect:/api/v1/books/%d/comments".formatted(bookId);
    }

    @PostMapping("/api/v1/books/{bookId}/comments/{commentId}/edit")
    public String updateAction(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId,
                               String description) {
        commentService.update(bookId, commentId, description);
        return "redirect:/api/v1/books/%d/comments".formatted(bookId);
    }

    @PostMapping("/api/v1/books/{bookId}/comments/{commentId}/delete")
    public String deleteAction(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return "redirect:/api/v1/books/%d/comments".formatted(bookId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
