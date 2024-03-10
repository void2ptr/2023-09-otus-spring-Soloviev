package ru.otus.hw.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/api/v1/books/{Id}/comments")
    @CircuitBreaker(name = "ControllerCommentFindCommentsByBookById", fallbackMethod = "recoverFindCommentsByBookById")
    public List<CommentDto> findCommentsByBookById(@PathVariable("Id") long id) {
        return commentService.findCommentByBookId(id);
    }

    @PostMapping("/api/v1/books/comments")
    @CircuitBreaker(name = "ControllerCommentInsert", fallbackMethod = "recoverInsertAction")
    public CommentDto insertAction(@RequestBody CommentDto commentDto) {
        return commentService.insert(commentDto);
    }

    @PutMapping("/api/v1/books/comments")
    @CircuitBreaker(name = "ControllerCommentUpdate", fallbackMethod = "recoverUpdateAction")
    public CommentDto updateAction(@RequestBody CommentDto commentDto) {
        return commentService.update(commentDto);
    }

    @DeleteMapping("/api/v1/books/comments/{commentId}")
    @CircuitBreaker(name = "ControllerCommentDelete", fallbackMethod = "recoverDeleteAction")
    public CommentDto deleteAction(@PathVariable("commentId") long commentId) {
        return commentService.delete(commentId);
    }

    private List<CommentDto> recoverFindCommentsByBookById(long id, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return commentService.findCommentByBookId(id);
    }

    private CommentDto recoverInsertAction(CommentDto commentDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return commentService.insert(commentDto);
    }

    private CommentDto recoverUpdateAction(CommentDto commentDto, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return commentService.update(commentDto);
    }

    private CommentDto recoverDeleteAction(long commentId, Exception ex) {
        log.warn(ex.getMessage(), ex);
        return commentService.delete(commentId);
    }

}
