package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/api/v1/books/{Id}/comments")
    public List<CommentDto> findCommentsByBookById(@PathVariable("Id") long id) {
        return commentService.findCommentByBookId(id);
    }

    @PostMapping("/api/v1/books/{id}/comments")
    public CommentDto insertAction(@PathVariable("id") long bookId, @RequestBody CommentDto commentDto) {
        return commentService.insert(commentDto);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public CommentDto updateAction(@PathVariable("bookId") long bookId, @PathVariable("commentId") long commentId,
                                   @RequestBody CommentDto commentDto) {
        return commentService.update(commentDto);
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{commentId}")
    public CommentDto deleteAction(@PathVariable("bookId") long bookId, @PathVariable("commentId") long commentId) {
        return commentService.delete(commentId);
    }
}
