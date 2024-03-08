package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.service.CommentService;


@RestController
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @GetMapping("/api/v1/books/{Id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Flux<CommentDto> findByBookId(@PathVariable("Id") Long id) {
        return commentService.findAllByBookId(id);
    }

    @PostMapping("/api/v1/books/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CommentDto> insert(@PathVariable("id") Long bookId, @RequestBody CommentDto commentDto) {
        return commentService.insert(commentDto);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<CommentDto> update(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId,
                                   @RequestBody CommentDto commentDto) {
        return commentService.update(commentDto);
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Boolean> delete(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId) {
        return commentService.delete(commentId);
    }
}
