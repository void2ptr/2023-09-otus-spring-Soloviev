package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.model.Comment;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getDescription(),
                new BookDto(comment.getBookId(),"",new AuthorDto(0L, ""), new ArrayList<>())
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getBook().getId(),
                commentDto.getDescription()
        );
    }
}
