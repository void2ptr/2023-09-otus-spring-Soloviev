package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Comment;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getDescription(),
                new BookDto(
                        comment.getBookModel().getId(),
                        comment.getBookModel().getTitle(),
                        new AuthorDto(
                                comment.getBookModel().getAuthor().getId(),
                                comment.getBookModel().getAuthor().getFullName()),
                        comment.getBookModel().getGenres()
                                .stream()
                                .map(g -> new GenreDto(g.getId(), g.getName()))
                                .collect(Collectors.toList())
                )
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getDescription(),
                BookMapper.toBook(commentDto.getBook())
        );
    }
}
