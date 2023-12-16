package ru.otus.hw.mappers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@AllArgsConstructor
@Getter
@Setter
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getDescription(),
                new Book(
                        comment.getBook().getId(),
                        comment.getBook().getTitle(),
                        comment.getBook().getAuthor(),
                        comment.getBook().getGenres()
                )
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getDescription(),
                commentDto.getBook()
        );
    }
}
