package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Comment;

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
                        comment.getBook().getId(),
                        comment.getBook().getTitle(),
                        new AuthorDto(
                                comment.getBook().getAuthor().getId(),
                                comment.getBook().getAuthor().getFullName()),
                        comment.getBook().getGenres()
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
