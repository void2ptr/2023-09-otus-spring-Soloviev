package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.Book;
import ru.otus.hw.model.Comment;
import ru.otus.hw.repository.BookRepository;
import ru.otus.hw.service.CommentService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    private final BookRepository bookRepository;

    @GetMapping("/books/{bookId}/comments")
    public String listPage(@PathVariable("bookId") long bookId, Model model) {
        List<CommentDto> commentsDto = commentService.findCommentByBookId(bookId);
        Optional<CommentDto> commentOpt = commentsDto.stream().findFirst();
        if (commentOpt.isEmpty()) {
            return "redirect:/books/%d/comments/add".formatted(bookId);
        }

        BookDto bookDto = commentOpt.orElseThrow(() ->
                        new EntityNotFoundException("Book for Comment with id '%d' not found".formatted(bookId)))
                .getBook();

        model.addAttribute("book", bookDto);
        model.addAttribute("comments", commentsDto);
        return "/comments/comments";
    }

    @GetMapping("/books/{bookId}/comments/add")
    public String addPage(@PathVariable("bookId") Long bookId, Model model) {
        BookDto bookDto = commentService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id '%d' not found".formatted(bookId)));
        CommentDto commentDto = new CommentDto(0, "", bookDto);
        model.addAttribute("book", bookDto);
        model.addAttribute("comment", commentDto);
        return "/comments/comment-add";
    }

    @GetMapping("/books/{bookId}/comments/{commentId}/edit")
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
        return "/comments/comment-edit";
    }

    @GetMapping("/books/{bookId}/comments/{commentId}/delete")
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
        return "/comments/comment-delete";
    }

    @PostMapping("/books/{bookId}/comments/add")
    public String insert(@PathVariable("bookId") Long bookId, String description) {
        commentService.insert(new Comment(0L, description, this.findBookById(bookId)));
        return "redirect:/books/%d/comments".formatted(bookId);
    }

    @PostMapping("/books/{bookId}/comments/{commentId}/edit")
    public String update(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId,
                         String description) {

        commentService.update(new Comment(commentId, description, findBookById(bookId)));
        return "redirect:/books/%d/comments".formatted(bookId);
    }

    @PostMapping("/books/{bookId}/comments/{commentId}/delete")
    public String delete(@PathVariable("bookId") Long bookId, @PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
        return "redirect:/books/%d/comments".formatted(bookId);
    }

    private Book findBookById(long bookId) {
        var bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: book '%d' not found".formatted(bookId));
        }
        return bookOpt.get();
    }

}
