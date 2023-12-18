package ru.otus.hw.controllers;

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
@RequestMapping("/api/v1/book")
public class CommentsController {

    private static final String API_PATH = "/api/v1/comment";

    private static final String ROOT_URL = "/api/v1/book";

    private final CommentService commentService;

    @GetMapping("/{bookId}/comment")
    public String listPage(@PathVariable("bookId") Long bookId, Model model) {
        List<CommentDto> comments = commentService.findCommentByBookId(bookId);
        Optional<CommentDto> commentOpt = comments.stream().findFirst();
        if (commentOpt.isEmpty()) {
            return "redirect:%s/%d/comment/0/add".formatted(ROOT_URL, bookId);
        }

        Book book = commentOpt.orElseThrow(() ->
                        new EntityNotFoundException("Book for Comment with id '%d' not found".formatted(bookId)))
                .getBook();

        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return API_PATH + "/comments";
    }

    @GetMapping("/{bookId}/comment/0/add")
    public String addPage(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDto = new CommentDto(0, "", BookMapper.toBook(bookDto));
        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDto);
        model.addAttribute("action", "add");
        return API_PATH + "/comment";
    }

    @GetMapping("/{bookId}/comment/{commentId}/edit")
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
        return API_PATH + "/comment";
    }

    @GetMapping("/{bookId}/comment/{commentId}/delete")
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
        return API_PATH + "/comment";
    }

    @PostMapping("/{bookId}/comment/0/add")
    public String insetAction(
            @PathVariable("bookId") Long bookId,
            CommentDto commentDto) {
        commentService.insert(
                bookId,
                commentDto.getDescription());
        return "redirect:%s/%d/comment".formatted(ROOT_URL, bookId);
    }

    @PostMapping("/{bookId}/comment/{commentId}/edit")
    public String updateAction(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId,
                               CommentDto commentDto) {
        commentService.update(
                bookId,
                commentId,
                commentDto.getDescription()
        );
        return "redirect:%s/%d/comment".formatted(ROOT_URL, bookId);
    }

    @PostMapping("{bookId}/comment/{commentId}/delete")
    public String deleteAction(
            @PathVariable("bookId") Long bookId,
            @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return "redirect:%s/%d/comment".formatted(ROOT_URL, bookId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
