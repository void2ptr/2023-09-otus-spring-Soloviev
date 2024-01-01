package ru.otus.hw.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.hw.model.Author;
import ru.otus.hw.dto.AuthorDto;

@AllArgsConstructor
@Getter
@Setter
public class AuthorMapper {

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }

    public static Author toAuthor(AuthorDto authorDto) {
        return new Author(
                authorDto.getId(),
                authorDto.getFullName()
        );
    }
}
